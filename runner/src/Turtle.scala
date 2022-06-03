package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class Turtle extends Agent {
  var ycord: Double                      = 0
  var FUNCTION_ARG_right_value: Double   = 0
  var color: Double                      = 0
  var FUNCTION_RETURN_right: Any         = 0
  var FUNCTION_ARG_forward_value: Double = 0
  var speed: Int                         = 0
  var FUNCTION_RETURN_fw: Any            = 0
  var FUNCTION_RETURN_left: Any          = 0
  var FUNCTION_RETURN_can_move: Boolean  = false
  var FUNCTION_RETURN_setxy: Any         = 0
  var FUNCTION_ARG_fct_value: Int        = 0
  var FUNCTION_ARG_setxy_y: Double       = 0
  var FUNCTION_ARG_fw_value: Double      = 0
  var FUNCTION_ARG_setxy_x: Double       = 0
  var angle: Double                      = 0
  var forward_m: Double                  = 0
  var FUNCTION_ARG_left_value: Double    = 0
  var FUNCTION_RETURN_home: Any          = 0
  var FUNCTION_RETURN_fct: Int           = 0
  var FUNCTION_RETURN_forward: Any       = 0
  var FUNCTION_RETURN_default_init: Any  = 0
  var xcord: Double                      = 0
  def get_ycord(): Double                = ycord
  def set_ycord(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("ycord") = DEFAULT_value
    ycord = DEFAULT_value
  }
  def get_FUNCTION_ARG_right_value(): Double = FUNCTION_ARG_right_value
  def set_FUNCTION_ARG_right_value(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_right_value") = DEFAULT_value
    FUNCTION_ARG_right_value = DEFAULT_value
  }
  def get_color(): Double = color
  def set_color(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("color") = DEFAULT_value
    color = DEFAULT_value
  }
  def get_FUNCTION_RETURN_right(): Any = FUNCTION_RETURN_right
  def set_FUNCTION_RETURN_right(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_right") = DEFAULT_value
    FUNCTION_RETURN_right = DEFAULT_value
  }
  def get_FUNCTION_ARG_forward_value(): Double = FUNCTION_ARG_forward_value
  def set_FUNCTION_ARG_forward_value(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_forward_value") = DEFAULT_value
    FUNCTION_ARG_forward_value = DEFAULT_value
  }
  def get_speed(): Int = speed
  def set_speed(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("speed") = DEFAULT_value
    speed = DEFAULT_value
  }
  def get_FUNCTION_RETURN_fw(): Any = FUNCTION_RETURN_fw
  def set_FUNCTION_RETURN_fw(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_fw") = DEFAULT_value
    FUNCTION_RETURN_fw = DEFAULT_value
  }
  def get_FUNCTION_RETURN_left(): Any = FUNCTION_RETURN_left
  def set_FUNCTION_RETURN_left(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_left") = DEFAULT_value
    FUNCTION_RETURN_left = DEFAULT_value
  }
  def get_FUNCTION_RETURN_can_move(): Boolean = FUNCTION_RETURN_can_move
  def set_FUNCTION_RETURN_can_move(DEFAULT_value: Boolean): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_can_move") = DEFAULT_value
    FUNCTION_RETURN_can_move = DEFAULT_value
  }
  def get_FUNCTION_RETURN_setxy(): Any = FUNCTION_RETURN_setxy
  def set_FUNCTION_RETURN_setxy(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_setxy") = DEFAULT_value
    FUNCTION_RETURN_setxy = DEFAULT_value
  }
  def get_FUNCTION_ARG_fct_value(): Int = FUNCTION_ARG_fct_value
  def set_FUNCTION_ARG_fct_value(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("FUNCTION_ARG_fct_value") = DEFAULT_value
    FUNCTION_ARG_fct_value = DEFAULT_value
  }
  def get_FUNCTION_ARG_setxy_y(): Double = FUNCTION_ARG_setxy_y
  def set_FUNCTION_ARG_setxy_y(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_setxy_y") = DEFAULT_value
    FUNCTION_ARG_setxy_y = DEFAULT_value
  }
  def get_FUNCTION_ARG_fw_value(): Double = FUNCTION_ARG_fw_value
  def set_FUNCTION_ARG_fw_value(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_fw_value") = DEFAULT_value
    FUNCTION_ARG_fw_value = DEFAULT_value
  }
  def get_FUNCTION_ARG_setxy_x(): Double = FUNCTION_ARG_setxy_x
  def set_FUNCTION_ARG_setxy_x(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_setxy_x") = DEFAULT_value
    FUNCTION_ARG_setxy_x = DEFAULT_value
  }
  def get_angle(): Double = angle
  def set_angle(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("angle") = DEFAULT_value
    angle = DEFAULT_value
  }
  def get_forward_m(): Double = forward_m
  def set_forward_m(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("forward_m") = DEFAULT_value
    forward_m = DEFAULT_value
  }
  def get_FUNCTION_ARG_left_value(): Double = FUNCTION_ARG_left_value
  def set_FUNCTION_ARG_left_value(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_left_value") = DEFAULT_value
    FUNCTION_ARG_left_value = DEFAULT_value
  }
  def get_FUNCTION_RETURN_home(): Any = FUNCTION_RETURN_home
  def set_FUNCTION_RETURN_home(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_home") = DEFAULT_value
    FUNCTION_RETURN_home = DEFAULT_value
  }
  def get_FUNCTION_RETURN_fct(): Int = FUNCTION_RETURN_fct
  def set_FUNCTION_RETURN_fct(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_fct") = DEFAULT_value
    FUNCTION_RETURN_fct = DEFAULT_value
  }
  def get_FUNCTION_RETURN_forward(): Any = FUNCTION_RETURN_forward
  def set_FUNCTION_RETURN_forward(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_forward") = DEFAULT_value
    FUNCTION_RETURN_forward = DEFAULT_value
  }
  def get_FUNCTION_RETURN_default_init(): Any = FUNCTION_RETURN_default_init
  def set_FUNCTION_RETURN_default_init(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_default_init") = DEFAULT_value
    FUNCTION_RETURN_default_init = DEFAULT_value
  }
  def get_xcord(): Double = xcord
  def set_xcord(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("xcord") = DEFAULT_value
    xcord = DEFAULT_value
  }
  override def main(): Unit = {
    markOverride("DEFAULT_UpdateFromParent", "DEFAULT_UpdateFromWorker")
    // lambda_17
    if (DEFAULT_INITER == 18) {
      {
        set_speed(0)
        set_xcord(0)
      }
    }
    while (true) {
      handleMessages()
      waitLabel(Turn, 1)
    }
  }
  def DEFAULT_PREDICATE_0(): Turtle = {
    if ((get_color() < 1)) this
    else null
  }
  override def DEFAULT_UpdateFromParent(dic: mutable.Map[String, Any]): Unit = {
    var values = dic.toList
    while (!values.isEmpty) {
      var kv = values.head
      values = values.tail
      if (kv._1 == "ycord") {
        ycord = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_ARG_right_value") {
        FUNCTION_ARG_right_value = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "color") {
        color = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_right") {
        FUNCTION_RETURN_right = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_ARG_forward_value") {
        FUNCTION_ARG_forward_value = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "speed") {
        speed = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "FUNCTION_RETURN_fw") {
        FUNCTION_RETURN_fw = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_left") {
        FUNCTION_RETURN_left = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_can_move") {
        FUNCTION_RETURN_can_move = kv._2.asInstanceOf[Boolean]
      }
      if (kv._1 == "FUNCTION_RETURN_setxy") {
        FUNCTION_RETURN_setxy = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_ARG_fct_value") {
        FUNCTION_ARG_fct_value = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "FUNCTION_ARG_setxy_y") {
        FUNCTION_ARG_setxy_y = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_ARG_fw_value") {
        FUNCTION_ARG_fw_value = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_ARG_setxy_x") {
        FUNCTION_ARG_setxy_x = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "angle") {
        angle = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "forward_m") {
        forward_m = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_ARG_left_value") {
        FUNCTION_ARG_left_value = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_home") {
        FUNCTION_RETURN_home = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_fct") {
        FUNCTION_RETURN_fct = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "FUNCTION_RETURN_forward") {
        FUNCTION_RETURN_forward = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_default_init") {
        FUNCTION_RETURN_default_init = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "xcord") {
        xcord = kv._2.asInstanceOf[Double]
      }
    }
  }
  override def DEFAULT_UpdateFromWorker(dic: mutable.Map[String, Any]): Unit = {
    var values = dic.toList
    while (!values.isEmpty) {
      var kv = values.head
      values = values.tail
      if (kv._1 == "ycord") {
        ycord = kv._2.asInstanceOf[Double]
        DEFAULT_logs("ycord") = ycord
      }
      if (kv._1 == "FUNCTION_ARG_right_value") {
        FUNCTION_ARG_right_value = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_right_value") = FUNCTION_ARG_right_value
      }
      if (kv._1 == "color") {
        color = kv._2.asInstanceOf[Double]
        DEFAULT_logs("color") = color
      }
      if (kv._1 == "FUNCTION_RETURN_right") {
        FUNCTION_RETURN_right = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_right") = FUNCTION_RETURN_right
      }
      if (kv._1 == "FUNCTION_ARG_forward_value") {
        FUNCTION_ARG_forward_value = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_forward_value") = FUNCTION_ARG_forward_value
      }
      if (kv._1 == "speed") {
        speed = kv._2.asInstanceOf[Int]
        DEFAULT_logs("speed") = speed
      }
      if (kv._1 == "FUNCTION_RETURN_fw") {
        FUNCTION_RETURN_fw = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_fw") = FUNCTION_RETURN_fw
      }
      if (kv._1 == "FUNCTION_RETURN_left") {
        FUNCTION_RETURN_left = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_left") = FUNCTION_RETURN_left
      }
      if (kv._1 == "FUNCTION_RETURN_can_move") {
        FUNCTION_RETURN_can_move = kv._2.asInstanceOf[Boolean]
        DEFAULT_logs("FUNCTION_RETURN_can_move") = FUNCTION_RETURN_can_move
      }
      if (kv._1 == "FUNCTION_RETURN_setxy") {
        FUNCTION_RETURN_setxy = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_setxy") = FUNCTION_RETURN_setxy
      }
      if (kv._1 == "FUNCTION_ARG_fct_value") {
        FUNCTION_ARG_fct_value = kv._2.asInstanceOf[Int]
        DEFAULT_logs("FUNCTION_ARG_fct_value") = FUNCTION_ARG_fct_value
      }
      if (kv._1 == "FUNCTION_ARG_setxy_y") {
        FUNCTION_ARG_setxy_y = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_setxy_y") = FUNCTION_ARG_setxy_y
      }
      if (kv._1 == "FUNCTION_ARG_fw_value") {
        FUNCTION_ARG_fw_value = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_fw_value") = FUNCTION_ARG_fw_value
      }
      if (kv._1 == "FUNCTION_ARG_setxy_x") {
        FUNCTION_ARG_setxy_x = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_setxy_x") = FUNCTION_ARG_setxy_x
      }
      if (kv._1 == "angle") {
        angle = kv._2.asInstanceOf[Double]
        DEFAULT_logs("angle") = angle
      }
      if (kv._1 == "forward_m") {
        forward_m = kv._2.asInstanceOf[Double]
        DEFAULT_logs("forward_m") = forward_m
      }
      if (kv._1 == "FUNCTION_ARG_left_value") {
        FUNCTION_ARG_left_value = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_left_value") = FUNCTION_ARG_left_value
      }
      if (kv._1 == "FUNCTION_RETURN_home") {
        FUNCTION_RETURN_home = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_home") = FUNCTION_RETURN_home
      }
      if (kv._1 == "FUNCTION_RETURN_fct") {
        FUNCTION_RETURN_fct = kv._2.asInstanceOf[Int]
        DEFAULT_logs("FUNCTION_RETURN_fct") = FUNCTION_RETURN_fct
      }
      if (kv._1 == "FUNCTION_RETURN_forward") {
        FUNCTION_RETURN_forward = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_forward") = FUNCTION_RETURN_forward
      }
      if (kv._1 == "FUNCTION_RETURN_default_init") {
        FUNCTION_RETURN_default_init = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_default_init") = FUNCTION_RETURN_default_init
      }
      if (kv._1 == "xcord") {
        xcord = kv._2.asInstanceOf[Double]
        DEFAULT_logs("xcord") = xcord
      }
    }
  }
}
