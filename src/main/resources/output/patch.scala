@lift
class Patch(val DEFAULT_observer: Observer, val DEFAULT_X: Int, val DEFAULT_Y: Int, val DEFAULT_INITER: Int) extends Actor{
	val DEFAULT_LOG_Variables = mutable.Map[String, Any]()
	var DEFAULT_ASK = -1
	var pycord : Any = 0
	var pxcord : Any = 0
	def get_pycord(): Any = pycord
	def set_pycord(DEFAULT_value : Any): Unit = {
		DEFAULT_LOG_Variables("pycord") = DEFAULT_value
		pycord = DEFAULT_value
	}
	def get_pxcord(): Any = pxcord
	def set_pxcord(DEFAULT_value : Any): Unit = {
		DEFAULT_LOG_Variables("pxcord") = DEFAULT_value
		pxcord = DEFAULT_value
	}
	def main():Unit = {
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def DEFAULT_UpdateFromParent(dic : mutable.Map[String, Any]):Unit = {
		dic.map((k,v) => k match{
			case "pycord" => pycord = v.asInstanceOf[Any]
			case "pxcord" => pxcord = v.asInstanceOf[Any]
		})
	}
	def DEFAULT_UpdateFromWorker(dic : mutable.Map[String, Any]):Unit = {
		dic.map((k,v) => k match{
			case "pycord" => set_pycord(v.asInstanceOf[Any])
			case "pxcord" => set_pxcord(v.asInstanceOf[Any])
		})
	}
}