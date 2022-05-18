@lift
class Patch(val DEFAULT_observer: Observer, val DEFAULT_X: Int, val DEFAULT_Y: Int, val DEFAULT_INITER: Int) extends Actor{
	var pycord : Any = 0
	var pxcord : Any = 0
	def get_pycord(): Any = pycord
	def set_pycord(__value : Any): Unit = {
		pycord = __value
	}
	def get_pxcord(): Any = pxcord
	def set_pxcord(__value : Any): Unit = {
		pxcord = __value
	}
	def main():Unit = {
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def DEFAULT_Update(dic : Any):Unit = {
		dic.map((k,v) => k match{
			case "pycord" => pycord = v.asInstanceOf[Any]
			case "pxcord" => pxcord = v.asInstanceOf[Any]
		})
	}
}