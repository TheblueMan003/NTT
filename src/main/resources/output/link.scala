package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class Link(val DEFAULT_observer: Observer, val DEFAULT_X: Int, val DEFAULT_Y: Int, val DEFAULT_INITER: Int) extends Agent(DEFAULT_observer, DEFAULT_X, DEFAULT_Y, DEFAULT_INITER){
	val DEFAULT_LOG_Variables = mutable.Map[String, Any]()
	var DEFAULT_ASK = -1
	def main():Unit = {
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def DEFAULT_UpdateFromParent(dic : mutable.Map[String, Any]):Unit = {
		dic.map{case (k, v) => k match{
			case "default_is_done" => default_is_done = v.asInstanceOf[Any]
		}}
	}
	def DEFAULT_UpdateFromWorker(dic : mutable.Map[String, Any]):Unit = {
		dic.map{case (k, v) => k match{
			case "default_is_done" => set_default_is_done(v.asInstanceOf[Any])
		}}
	}
}