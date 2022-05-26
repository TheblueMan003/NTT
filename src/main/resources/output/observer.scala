package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class Observer(val DEFAULT_BOARD_X: Int, val DEFAULT_BOARD_Y: Int){
	val wolves = mutable.Set[Wolf]()
	def get_wolves(): mutable.Set[Wolf] = wolves
	val agents = mutable.Set[Agent]()
	def get_agents(): mutable.Set[Agent] = agents
	val turtles = mutable.Set[Turtle]()
	def get_turtles(): mutable.Set[Turtle] = turtles
	val patches = mutable.Set[Patch]()
	def get_patches(): mutable.Set[Patch] = patches
	val observers = mutable.Set[Observer]()
	def get_observers(): mutable.Set[Observer] = observers
	val linkes = mutable.Set[Link]()
	def get_linkes(): mutable.Set[Link] = linkes
	val DEFAULT_LOG_Variables = mutable.Map[String, Any]()
	var DEFAULT_ASK = -1
	var test : Any = 0
	var go_m : Int = 0
	var index : Any = 0
	var go_c : Any = 0
	def get_test(): Any = test
	def set_test(DEFAULT_value : Any): Unit = {
		DEFAULT_LOG_Variables("test") = DEFAULT_value
		test = DEFAULT_value
	}
	def get_go_m(): Int = go_m
	def set_go_m(DEFAULT_value : Int): Unit = {
		DEFAULT_LOG_Variables("go_m") = DEFAULT_value
		go_m = DEFAULT_value
	}
	def get_index(): Any = index
	def set_index(DEFAULT_value : Any): Unit = {
		DEFAULT_LOG_Variables("index") = DEFAULT_value
		index = DEFAULT_value
	}
	def get_go_c(): Any = go_c
	def set_go_c(DEFAULT_value : Any): Unit = {
		DEFAULT_LOG_Variables("go_c") = DEFAULT_value
		go_c = DEFAULT_value
	}
	def main():Unit = {
		setup()
		while(true){
			set_go_m(1)
			println("start of tick")
			val tmp_6 = DEFAULT_observer.get_turtles().toList.map(s => asyncMessage(() => s.lambda_0(this)))
			while(!tmp_6.forall(_.isCompleted)){
				waitAndReply(1)
			}
			println("middle of tick")
			val tmp_7 = DEFAULT_observer.get_turtles().toList.map(s => asyncMessage(() => s.lambda_2(this)))
			while(!tmp_7.forall(_.isCompleted)){
				waitAndReply(1)
			}
			println("end of tick")
			val tmp_8 = DEFAULT_observer.get_turtles().toList.map(a => asyncMessage(() => a.get_speed))
			while (!(tmp_8.nonEmpty && tmp_8.forall(x => x.isCompleted))){
				waitAndReply(1)
			}
			val tmp_9: List[Int] = tmp_8.map(o => o.popValue.get).asInstanceOf[List[Int]]
			set_go_c(tmp_9)
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def DEFAULT_UpdateFromParent(dic : mutable.Map[String, Any]):Unit = {
		dic.map{case (k, v) => k match{
			case "DEFAULT_BOARD_Y" => DEFAULT_BOARD_Y = v.asInstanceOf[Int]
			case "DEFAULT_BOARD_X" => DEFAULT_BOARD_X = v.asInstanceOf[Int]
			case "test" => test = v.asInstanceOf[Any]
			case "go_m" => go_m = v.asInstanceOf[Int]
			case "index" => index = v.asInstanceOf[Any]
			case "go_c" => go_c = v.asInstanceOf[Any]
			case "default_is_done" => default_is_done = v.asInstanceOf[Any]
		}}
	}
	def DEFAULT_UpdateFromWorker(dic : mutable.Map[String, Any]):Unit = {
		dic.map{case (k, v) => k match{
			case "DEFAULT_BOARD_Y" => set_DEFAULT_BOARD_Y(v.asInstanceOf[Int])
			case "DEFAULT_BOARD_X" => set_DEFAULT_BOARD_X(v.asInstanceOf[Int])
			case "test" => set_test(v.asInstanceOf[Any])
			case "go_m" => set_go_m(v.asInstanceOf[Int])
			case "index" => set_index(v.asInstanceOf[Any])
			case "go_c" => set_go_c(v.asInstanceOf[Any])
			case "default_is_done" => set_default_is_done(v.asInstanceOf[Any])
		}}
	}
	def setup():Unit = {
		(1 to 10).map(_ =>{
			turtles.add(new Turtle(this, 0, 0, 3))
		})
	}
}