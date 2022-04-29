@Lift
class observer{
	val agents = Mutable.Set[Agent]()
	val turtles = Mutable.Set[Turtle]()
	val wolves = Mutable.Set[Wolf]()
	val patches = Mutable.Set[Patch]()
	val linkes = Mutable.Set[Link]()
	val observers = Mutable.Set[Observer]()
	var test : Int = 0
	var index : Int = 0
	def get_test(): Int = test
	def set_test(__value__ : Int): Unit = {
		test = __value__
	}
	def get_index(): Int = index
	def set_index(__value__ : Int): Unit = {
		index = __value__
	}
	def main():Unit = {
		setup()
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def setup():Unit = {
		val __tmp__ = new turtle()
	}
}