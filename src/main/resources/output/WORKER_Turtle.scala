@lift
class WORKER_Turtle(val DEFAULT_Parent: Turtle, val DEFAULT_logs: mutable.Map[String, Any], val DEFAULT_ASK: Int) extends Turtle{
	def main():Unit = {
		DEFAULT_UpdateFromParent(DEFAULT_logs)
		while(true){
			handleMessages()
			DEFAULT_ASK match{
				case 2 => {
					left(90.0)
					println("turn")
				}
				case 1 => {
					forward(1.0)
					println("walk")
				}
				case 0 => {
					val tmp_2 = get_turtles.toList.map(s => asyncMessage(() => s.lambda_1(this)))
					while(!tmp_2.forall(_.isCompleted)){
						waitAndReply(1)
					}
				}
			}
			waitLabel(Turn, 1)
		}
	}
}