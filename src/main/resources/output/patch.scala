@lift
class Patch extends Actor{
	def main():Unit = {
		
		while(true){
			
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
}