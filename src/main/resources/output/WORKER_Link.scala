@lift
class WORKER_Link(val DEFAULT_Parent: Link, val DEFAULT_logs: mutable.Map[String, Any], val DEFAULT_ASK: Int) extends Link{
	def main():Unit = {
		DEFAULT_UpdateFromParent(DEFAULT_logs)
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
}