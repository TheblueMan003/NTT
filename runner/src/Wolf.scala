package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class Wolf extends Turtle {
  override def main(): Unit = {
    markOverride("DEFAULT_UpdateFromParent", "DEFAULT_UpdateFromWorker")
    while (true) {
      handleMessages()
      waitLabel(Turn, 1)
    }
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
