@lift
class Turtle(val DEFAULT_observer: Observer, val DEFAULT_X: Int, val DEFAULT_Y: Int, val DEFAULT_INITER: Int) extends Actor{
	val DEFAULT_LOG_Variables = mutable.Map[String, Any]()
	var DEFAULT_ASK = -1
	var ycord : Double = 0
	var color : Any = 0
	var arg_m : Double = 0
	var angle : Double = 0
	var forward_m : Double = 0
	var argtest_m : Double = 0
	var speed : Double = 0
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
	def get_arg_m(): Double = arg_m
	def set_arg_m(DEFAULT_value : Double): Unit = {
		DEFAULT_LOG_Variables("arg_m") = DEFAULT_value
		arg_m = DEFAULT_value
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
	def get_argtest_m(): Double = argtest_m
	def set_argtest_m(DEFAULT_value : Double): Unit = {
		DEFAULT_LOG_Variables("argtest_m") = DEFAULT_value
		argtest_m = DEFAULT_value
	}
	def get_speed(): Double = speed
	def set_speed(DEFAULT_value : Double): Unit = {
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
					val tmp_0 = get_turtles.toList.map(s => asyncMessage(() => s.lambda_1(this)))
					while(!tmp_0.forall(_.isCompleted)){
						waitAndReply(1)
					}
				}
			}
			waitLabel(Turn, 1)
		}
	}
	def DEFAULT_UpdateFromParent(dic : mutable.Map[String, Any]):Unit = {
		dic.map((k,v) => k match{
			case "ycord" => ycord = v.asInstanceOf[Double]
			case "color" => color = v.asInstanceOf[Any]
			case "arg_m" => arg_m = v.asInstanceOf[Double]
			case "angle" => angle = v.asInstanceOf[Double]
			case "forward_m" => forward_m = v.asInstanceOf[Double]
			case "argtest_m" => argtest_m = v.asInstanceOf[Double]
			case "speed" => speed = v.asInstanceOf[Double]
			case "xcord" => xcord = v.asInstanceOf[Double]
		})
	}
	def DEFAULT_UpdateFromWorker(dic : mutable.Map[String, Any]):Unit = {
		dic.map((k,v) => k match{
			case "ycord" => set_ycord(v.asInstanceOf[Double])
			case "color" => set_color(v.asInstanceOf[Any])
			case "arg_m" => set_arg_m(v.asInstanceOf[Double])
			case "angle" => set_angle(v.asInstanceOf[Double])
			case "forward_m" => set_forward_m(v.asInstanceOf[Double])
			case "argtest_m" => set_argtest_m(v.asInstanceOf[Double])
			case "speed" => set_speed(v.asInstanceOf[Double])
			case "xcord" => set_xcord(v.asInstanceOf[Double])
		})
	}
	def forward(value : Double):Unit = {
		set_forward_m((set_value() * Math.cos(set_angle())))
		set_xcord((set_xcord() + set_forward_m()))
		set_forward_m((set_value() * Math.sin(set_angle())))
		set_ycord((set_ycord() + set_forward_m()))
	}
	def setxy(x : Any, y : Any):Unit = {
		set_xcord(set_x())
		set_ycord(set_y())
	}
	def right(value : Int):Unit = {
		set_angle((set_angle() - set_value()))
	}
	def argtest(value : Double):Unit = {
		set_argtest_m(5)
		set_argtest_m(0.0)
		if((InstructionList(List(codegen.EmptyInstruction$@11d0e57d, codegen.EmptyInstruction$@11d0e57d)),(set_value() == 0.0))){
			if((InstructionList(List(codegen.EmptyInstruction$@11d0e57d, codegen.EmptyInstruction$@11d0e57d)),(set_argtest_m() == 0))){
				right(5)
			}
		}
		else if((InstructionList(List(codegen.EmptyInstruction$@11d0e57d, codegen.EmptyInstruction$@11d0e57d)),(set_value() > 2.0))){
			left(5)
		}
		else{
			fw(5)
		}
	}
	def home():Unit = {
		set_xcord(0.0)
		set_ycord(0.0)
	}
	def fw(value : Int):Unit = {
		forward(set_value())
	}
	def lambda_3():Unit = {
		set_speed(0)
		set_xcord(0)
	}
	def lambda_2(__myself_0 : Any):Unit = {
		left(90.0)
		println("turn")
	}
	def left(value : Double):Unit = {
		set_angle((set_angle() + set_value()))
	}
	def lambda_1(__myself_1 : Any, __myself_0 : Any):Unit = {
		forward(1.0)
		println("walk")
	}
	def lambda_0(__myself_0 : Any):Unit = {
		val tmp_1 = get_turtles.toList.map(s => asyncMessage(() => s.lambda_1(this)))
		while(!tmp_1.forall(_.isCompleted)){
			waitAndReply(1)
		}
	}
	def arg():Unit = {
		set_arg_m(5.0)
		set_arg_m(0)
		right(0)
		fw(1)
		argtest(5.0)
		argtest(5)
		set_speed(0.5)
	}
	def __init__():Unit = {
		set_angle(0.0)
	}
	def can_move():Boolean = {
		set_angle(set_angle())
		true
	}
}