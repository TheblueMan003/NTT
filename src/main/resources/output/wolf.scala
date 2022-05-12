@lift
class Wolf extends Turtle{
	def override_main():Unit = {
		
		while(true){
			
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
}