@lift
class observer{
	var test : Int = 0
	var index : Int = 0
	def main():Unit = {
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def setup():Unit = {
		val __tmp__ = new turtle()
	}
}