@lift
class Observer extends Actor{
	val wolves = mutable.Set[Wolf]()
	val patches = mutable.Set[Patch]()
	val observers = mutable.Set[Observer]()
	val linkes = mutable.Set[Link]()
	val turtles = mutable.Set[Turtle]()
	val agents = mutable.Set[Agent]()
	var test : Any = 0
	var go_m : Int = 0
	var index : Any = 0
	var go_c : List[Double] = Nil
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
	def get_go_c(): List[Double] = go_c
	def set_go_c(__value : List[Double]): Unit = {
		go_c = __value
	}
	def main():Unit = {
		setup()
		while(true){
			go_m = 1
			println("start of tick")
			val tmp_0 = turtles.toList.map(s => asyncMessage(() => s.lambda_0(this)))
			while(!tmp_0.forall(_.isCompleted)){
				waitAndReply(1)
			}
			println("middle of tick")
			val tmp_1 = turtles.toList.map(s => asyncMessage(() => s.lambda_1(this)))
			while(!tmp_1.forall(_.isCompleted)){
				waitAndReply(1)
			}
			println("end of tick")
			val tmp_2 = turtles.toList.map(a => asyncMessage(() => a.get_speed))
			while (!(tmp_2.nonEmpty && tmp_2.forall(x => x.isCompleted))){
				waitAndReply(1)
			}
			val tmp_3: List[Double] = tmp_2.map(o => o.popValue.get).asInstanceOf[List[Double]]
			go_c = tmp_3
			handleMessages()
			DEFAULT_ASK match{

			}
			waitLabel(Turn, 1)
		}
	}
	def DEFAULT_Update(dic : Any):Unit = {
		dic.map((k,v) => k match{
			case "test" => test = v.asInstanceOf[Any]
			case "go_m" => go_m = v.asInstanceOf[Int]
			case "index" => index = v.asInstanceOf[Any]
			case "go_c" => go_c = v.asInstanceOf[List[Double]]
		})
	}
	def setup():Unit = {
		turtles.add(new Turtle())
	}
}