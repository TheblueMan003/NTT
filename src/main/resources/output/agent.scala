@Lift
class Agent{
	var go_m : Int = 0
	def get_go_m(): Int = go_m
	def set_go_m(__value__ : Int): Unit = {
		go_m = __value__
	}
	def main():Unit = {
		
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def go():Unit = {
		go_m = 1

		val tmp = turtles.map(s => asyncMessage(() => s.lambda_0(this)))
		while(tmp.any(!_.isCompleted)){
			waitAndReply(1)
		}
	}
}