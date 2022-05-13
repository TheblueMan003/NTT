@lift
class Patch extends Actor{
	def main():Unit = {
		while(true){
			handleMessages()
			DEFAULT_ASK match{

			}
			waitLabel(Turn, 1)
		}
	}
}