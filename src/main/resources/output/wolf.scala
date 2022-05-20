@lift
class Wolf(val DEFAULT_observer: Observer, val DEFAULT_X: Int, val DEFAULT_Y: Int, val DEFAULT_INITER: Int) extends Turtle{
	val DEFAULT_LOG_Variables = mutable.Map[String, Any]()
	var DEFAULT_ASK = -1
	def override_main():Unit = {
		while(true){
			handleMessages()
			waitLabel(Turn, 1)
		}
	}
	def DEFAULT_UpdateFromParent(dic : mutable.Map[String, Any]):Unit = {
		dic.map((k,v) => k match{
			case "ycord" => ycord = v.asInstanceOf[Double]
			case "color" => color = v.asInstanceOf[Any]
			case "arg_m" => arg_m = v.asInstanceOf[Double]
			case "angle" => angle = v.asInstanceOf[Double]
			case "forward_m" => forward_m = v.asInstanceOf[Double]
			case "argtest_m" => argtest_m = v.asInstanceOf[Double]
			case "speed" => speed = v.asInstanceOf[Double]
			case "xcord" => xcord = v.asInstanceOf[Double]
		})
	}
	def DEFAULT_UpdateFromWorker(dic : mutable.Map[String, Any]):Unit = {
		dic.map((k,v) => k match{
			case "ycord" => set_ycord(v.asInstanceOf[Double])
			case "color" => set_color(v.asInstanceOf[Any])
			case "arg_m" => set_arg_m(v.asInstanceOf[Double])
			case "angle" => set_angle(v.asInstanceOf[Double])
			case "forward_m" => set_forward_m(v.asInstanceOf[Double])
			case "argtest_m" => set_argtest_m(v.asInstanceOf[Double])
			case "speed" => set_speed(v.asInstanceOf[Double])
			case "xcord" => set_xcord(v.asInstanceOf[Double])
		})
	}
}