@lift
class Wolf(val DEFAULT_observer: Observer, val DEFAULT_X: Int, val DEFAULT_Y: Int, val DEFAULT_INITER: Int) extends Turtle{
	def override_main():Unit = {
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def DEFAULT_Update(dic : Any):Unit = {
		dic.map((k,v) => k match{

		})
	}
}