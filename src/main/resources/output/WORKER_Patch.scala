@lift
class WORKER_Patch(val DEFAULT_Parent: Patch, val DEFAULT_logs: mutable.Map[String, Any], val DEFAULT_ASK: Int) extends Patch{
	def main():Unit = {
		DEFAULT_UpdateFromParent(DEFAULT_logs)
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
}