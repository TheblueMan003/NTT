@Lift
class Turtle{
	var arg_m : Float = 0
	var argtest_m : Float = 0
	var speed : Int = 0
	def get_arg_m(): Float = arg_m
	def set_arg_m(__value__ : Float): Unit = {
		arg_m = __value__
	}
	def get_argtest_m(): Float = argtest_m
	def set_argtest_m(__value__ : Float): Unit = {
		argtest_m = __value__
	}
	def get_speed(): Int = speed
	def set_speed(__value__ : Int): Unit = {
		speed = __value__
	}
	def main():Unit = {
		
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def lambda_0():Unit = {
		speed = 0
		xcor = 0
	}
	def arg():Unit = {
		arg_m = 5.0
		arg_m = 0
		right(0)
		fw(1)
		argtest(5.0)
		argtest(5)
		show("test")
	}
	def argtest():Unit = {
		argtest_m = 5
		argtest_m = 0.0

		if((value == 0.0)){
			if((argtest_m == 0)){
				right(5)
			}
		}
		else if((value > 2.0)){
			left(5)
		}
		else{
			fw(5)
		}
	}
}