@lift
class WORKER_Wolf(val DEFAULT_Parent: Wolf, val DEFAULT_logs: mutable.Map[String, Any], val DEFAULT_ASK: Int) extends Wolf{
	def override_main():Unit = {
		DEFAULT_UpdateFromParent(DEFAULT_logs)
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
}