package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class WORKER_Turtle(val DEFAULT_observer: Observer, val DEFAULT_Parent: Turtle, val DEFAULT_logs: mutable.Map[String, Any], val DEFAULT_ASK: Int) extends Turtle(DEFAULT_observer, 0, 0, -1){
	def main():Unit = {
		DEFAULT_UpdateFromParent(DEFAULT_logs)
		while(true){
			handleMessages()
			if (DEFAULT_ASK == 2){
				left(90.0)
				println("turn")
			}
			if (DEFAULT_ASK == 1){
				forward(1.0)
				println("walk")
			}
			if (DEFAULT_ASK == 0){
				val tmp_3 = DEFAULT_observer.get_turtles().toList.map(s => WORKER_Turtle(DEFAULT_observer, this, DEFAULT_logs, 1))
				var tmp_5 = false
				while(!tmp_5){
					val tmp_4 = tmp_3.map(s => asyncMessage(() => s.get_default_is_done()))
					while(!tmp_3.forall(_.isCompleted)){
						waitAndReply(1)
					}
					tmp_5 = tmp_4.map(o => o.popValue.get).asInstanceOf[List[Boolean]].all(_)
				}
			}
			default_is_done = true
			waitLabel(Turn, 1)
		}
	}
}