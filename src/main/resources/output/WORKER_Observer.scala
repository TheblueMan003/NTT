@lift
class WORKER_Observer(val DEFAULT_Parent: Observer, val DEFAULT_logs: mutable.Map[String, Any], val DEFAULT_ASK: Int) extends Observer{
	def main():Unit = {
		DEFAULT_UpdateFromParent(DEFAULT_logs)
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
}