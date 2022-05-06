@lift
class Observer extends Actor{
	val patches = mutable.Set[Patch]()
	val observers = mutable.Set[Observer]()
	val linkes = mutable.Set[Link]()
	val turtles = mutable.Set[Turtle]()
	val agents = mutable.Set[Agent]()
	var test : Any = 0
	var go_m : Int = 0
	var index : Any = 0
	def get_test(): Any = test
	def set_test(__value : Any): Unit = {
		test = __value
	}
	def get_go_m(): Int = go_m
	def set_go_m(__value : Int): Unit = {
		go_m = __value
	}
	def get_index(): Any = index
	def set_index(__value : Any): Unit = {
		index = __value
	}
	def main():Unit = {
		setup()
		while(true){

			go_m = 1
			println("start of tick")

			val tmp1 = turtles.toList.map(s => asyncMessage(() => s.lambda_0(this)))
			while(!tmp1.forall(_.isCompleted)){
				waitAndReply(1)
			}
			println("middle of tick")

			val tmp2 = turtles.toList.map(s => asyncMessage(() => s.lambda_1(this)))
			while(!tmp2.forall(_.isCompleted)){
				waitAndReply(1)
			}
			println("end of tick")
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def setup():Unit = {

		turtles.add(new Turtle())
	}
}