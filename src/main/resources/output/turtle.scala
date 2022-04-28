@lift
class turtle{
	var color : Int = 0
	var ycor : Float = 0
	var xcor : Float = 0
	var arg_m : Float = 0
	var argtest_m : Float = 0
	var speed : Int = 0
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