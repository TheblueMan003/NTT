package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class Wolf(val DEFAULT_observer: Observer, val DEFAULT_X: Int, val DEFAULT_Y: Int, val DEFAULT_INITER: Int) extends Turtle(DEFAULT_observer, DEFAULT_X, DEFAULT_Y, DEFAULT_INITER){
	val DEFAULT_LOG_Variables = mutable.Map[String, Any]()
	var DEFAULT_ASK = -1
	def override_main():Unit = {
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def DEFAULT_UpdateFromParent(dic : mutable.Map[String, Any]):Unit = {
		dic.map{case (k, v) => k match{
			case "ycord" => ycord = v.asInstanceOf[Double]
			case "color" => color = v.asInstanceOf[Any]
			case "angle" => angle = v.asInstanceOf[Double]
			case "forward_m" => forward_m = v.asInstanceOf[Double]
			case "speed" => speed = v.asInstanceOf[Int]
			case "xcord" => xcord = v.asInstanceOf[Double]
			case "default_is_done" => default_is_done = v.asInstanceOf[Any]
		}}
	}
	def DEFAULT_UpdateFromWorker(dic : mutable.Map[String, Any]):Unit = {
		dic.map{case (k, v) => k match{
			case "ycord" => set_ycord(v.asInstanceOf[Double])
			case "color" => set_color(v.asInstanceOf[Any])
			case "angle" => set_angle(v.asInstanceOf[Double])
			case "forward_m" => set_forward_m(v.asInstanceOf[Double])
			case "speed" => set_speed(v.asInstanceOf[Int])
			case "xcord" => set_xcord(v.asInstanceOf[Double])
			case "default_is_done" => set_default_is_done(v.asInstanceOf[Any])
		}}
	}
}