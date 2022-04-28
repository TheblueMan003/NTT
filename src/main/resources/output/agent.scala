@lift
class agent{
	var go_m : Int = 0
	def main():Unit = {
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def go():Unit = {
		go_m = 1
	}
}