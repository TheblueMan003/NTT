@lift
class Agent{
	def main():Unit = {
		while(true){
			handleMessages()
			DEFAULT_ASK match{

			}
			waitLabel(Turn, 1)
		}
	}
	def DEFAULT_Update(dic : Any):Unit = {
		dic.map((k,v) => k match{

		})
	}
}