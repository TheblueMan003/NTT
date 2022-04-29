@Lift
class Patch{
	def main():Unit = {
		
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
}