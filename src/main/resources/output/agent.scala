package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class Agent(val DEFAULT_observer: Observer, val DEFAULT_X: Int, val DEFAULT_Y: Int, val DEFAULT_INITER: Int){
	val DEFAULT_LOG_Variables = mutable.Map[String, Any]()
	var DEFAULT_ASK = -1
	var default_is_done : Any = 0
	def get_default_is_done(): Any = default_is_done
	def set_default_is_done(DEFAULT_value : Any): Unit = {
		DEFAULT_LOG_Variables("default_is_done") = DEFAULT_value
		default_is_done = DEFAULT_value
	}
	def main():Unit = {
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def DEFAULT_UpdateFromParent(dic : mutable.Map[String, Any]):Unit = {
		dic.map(kv => {
			if(kv._1 == "default_is_done"){
				default_is_done = kv._2.asInstanceOf[Any]
			}
		})
	}
	def DEFAULT_UpdateFromParent(dic : mutable.Map[String, Any]):Unit = {
		dic.map(kv => {
			if(kv._1 == "default_is_done"){
				set_default_is_done(kv._2.asInstanceOf[Any])
			}
		})
	}
	def fct(value : Int):Unit = {
		value=0
		val tmp_0 = 5
		tmp_0
	}
}