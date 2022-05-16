@lift
class Turtle extends Actor{
	var ycord : Double = 0
	var color : Any = 0
	var arg_m : Double = 0
	var angle : Double = 0
	var forward_m : Double = 0
	var argtest_m : Double = 0
	var speed : Double = 0
	var xcord : Double = 0
	def get_ycord(): Double = ycord
	def set_ycord(__value : Double): Unit = {
		ycord = __value
	}
	def get_color(): Any = color
	def set_color(__value : Any): Unit = {
		color = __value
	}
	def get_arg_m(): Double = arg_m
	def set_arg_m(__value : Double): Unit = {
		arg_m = __value
	}
	def get_angle(): Double = angle
	def set_angle(__value : Double): Unit = {
		angle = __value
	}
	def get_forward_m(): Double = forward_m
	def set_forward_m(__value : Double): Unit = {
		forward_m = __value
	}
	def get_argtest_m(): Double = argtest_m
	def set_argtest_m(__value : Double): Unit = {
		argtest_m = __value
	}
	def get_speed(): Double = speed
	def set_speed(__value : Double): Unit = {
		speed = __value
	}
	def get_xcord(): Double = xcord
	def set_xcord(__value : Double): Unit = {
		xcord = __value
	}
	def main():Unit = {
		while(true){
			handleMessages()
			DEFAULT_ASK match{
				case 0 => {
					speed = 0
					xcord = 0
				}
				case 1 => {
					left(90.0)
					println("turn")
				}
				case 2 => {
					forward(1.0)
					println("walk")
				}
			}
			waitLabel(Turn, 1)
		}
	}
	def DEFAULT_Update(dic : Any):Unit = {
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
	def forward(value : Double):Unit = {
		forward_m = (value * Math.cos(angle))
		xcord = (xcord + forward_m)
		forward_m = (value * Math.sin(angle))
		ycord = (ycord + forward_m)
	}
	def setxy(x : Any, y : Any):Unit = {
		xcord = x
		ycord = y
	}
	def right(value : Int):Unit = {
		angle = (angle - value)
	}
	def argtest(value : Double):Unit = {
		argtest_m = 5
		argtest_m = 0.0
		if((InstructionList(List(codegen.EmptyInstruction$@1b2750e, codegen.EmptyInstruction$@1b2750e)),(value == 0.0))){
			if((InstructionList(List(codegen.EmptyInstruction$@1b2750e, codegen.EmptyInstruction$@1b2750e)),(argtest_m == 0))){
				right(5)
			}
		}
		else if((InstructionList(List(codegen.EmptyInstruction$@1b2750e, codegen.EmptyInstruction$@1b2750e)),(value > 2.0))){
			left(5)
		}
		else{
			fw(5)
		}
	}
	def home():Unit = {
		xcord = 0.0
		ycord = 0.0
	}
	def fw(value : Int):Unit = {
		forward(value)
	}
	def lambda_2():Unit = {
		speed = 0
		xcord = 0
	}
	def left(value : Double):Unit = {
		angle = (angle + value)
	}
	def lambda_1(__myself_0 : Any):Unit = {
		left(90.0)
		println("turn")
	}
	def lambda_0(__myself_0 : Any):Unit = {
		forward(1.0)
		println("walk")
	}
	def arg():Unit = {
		arg_m = 5.0
		arg_m = 0
		right(0)
		fw(1)
		argtest(5.0)
		argtest(5)
		speed = 0.5
	}
	def __init__():Unit = {
		angle = 0.0
	}
	def can_move():Boolean = {
		angle = angle
		true
	}
}