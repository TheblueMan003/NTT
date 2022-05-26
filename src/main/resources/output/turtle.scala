package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class Turtle(val DEFAULT_observer: Observer, val DEFAULT_X: Int, val DEFAULT_Y: Int, val DEFAULT_INITER: Int) extends Agent(DEFAULT_observer, DEFAULT_X, DEFAULT_Y, DEFAULT_INITER){
	val DEFAULT_LOG_Variables = mutable.Map[String, Any]()
	var DEFAULT_ASK = -1
	var ycord : Double = 0
	var color : Any = 0
	var angle : Double = 0
	var forward_m : Double = 0
	var speed : Int = 0
	var xcord : Double = 0
	def get_ycord(): Double = ycord
	def set_ycord(DEFAULT_value : Double): Unit = {
		DEFAULT_LOG_Variables("ycord") = DEFAULT_value
		ycord = DEFAULT_value
	}
	def get_color(): Any = color
	def set_color(DEFAULT_value : Any): Unit = {
		DEFAULT_LOG_Variables("color") = DEFAULT_value
		color = DEFAULT_value
	}
	def get_angle(): Double = angle
	def set_angle(DEFAULT_value : Double): Unit = {
		DEFAULT_LOG_Variables("angle") = DEFAULT_value
		angle = DEFAULT_value
	}
	def get_forward_m(): Double = forward_m
	def set_forward_m(DEFAULT_value : Double): Unit = {
		DEFAULT_LOG_Variables("forward_m") = DEFAULT_value
		forward_m = DEFAULT_value
	}
	def get_speed(): Int = speed
	def set_speed(DEFAULT_value : Int): Unit = {
		DEFAULT_LOG_Variables("speed") = DEFAULT_value
		speed = DEFAULT_value
	}
	def get_xcord(): Double = xcord
	def set_xcord(DEFAULT_value : Double): Unit = {
		DEFAULT_LOG_Variables("xcord") = DEFAULT_value
		xcord = DEFAULT_value
	}
	def main():Unit = {
		DEFAULT_INITER match{
			case 3 => {
				set_speed(0)
				set_xcord(0)
			}
		}
		while(true){
			handleMessages()
			DEFAULT_ASK match{
				case 2 => {
					left(90.0)
					println("turn")
				}
				case 1 => {
					forward(1.0)
					println("walk")
				}
				case 0 => {
					val tmp_0 = DEFAULT_observer.get_turtles().toList.map(s => WORKER_Turtle(DEFAULT_observer, this, DEFAULT_logs, 1))
					var tmp_2 = false
					while(!tmp_2){
						val tmp_1 = tmp_0.map(s => asyncMessage(() => s.get_default_is_done()))
						while(!tmp_0.forall(_.isCompleted)){
							waitAndReply(1)
						}
						tmp_2 = tmp_1.map(o => o.popValue.get).asInstanceOf[List[Boolean]].forall(_)
					}
				}
			}
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
	def forward(value : Double):Unit = {
		set_forward_m((value * Math.cos(get_angle())))
		set_xcord((get_xcord() + get_forward_m()))
		set_forward_m((value * Math.sin(get_angle())))
		set_ycord((get_ycord() + get_forward_m()))
	}
	def setxy(x : Any, y : Any):Unit = {
		set_xcord(x)
		set_ycord(y)
	}
	def right(value : Any):Unit = {
		set_angle((get_angle() - value))
	}
	def home():Unit = {
		set_xcord(0.0)
		set_ycord(0.0)
	}
	def fw(value : Any):Unit = {
		forward(value)
	}
	def left(value : Double):Unit = {
		set_angle((get_angle() + value))
	}
	def default_init():Unit = {
		set_angle(0.0)
	}
	def can_move():Boolean = {
		set_angle(get_angle())
		true
	}
}