@lift
class WORKER_Agent(val DEFAULT_Parent: Agent, val DEFAULT_logs: mutable.Map[String, Any], val DEFAULT_ASK: Int) extends Agent{
	def main():Unit = {
		DEFAULT_UpdateFromParent(DEFAULT_logs)
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
}