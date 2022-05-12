@lift
class Agent{
	def main():Unit = {
		
		while(true){
			
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
}