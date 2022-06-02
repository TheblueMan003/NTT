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
      if (kv._1 == "FUNCTION_ARG_forward_value") {
        FUNCTION_ARG_forward_value = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "speed") {
        speed = kv._2.asInstanceOf[Int]
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
      if (kv._1 == "xcord") {
        xcord = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_ARG_fct_value") {
        FUNCTION_ARG_fct_value = kv._2.asInstanceOf[Any]
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
      if (kv._1 == "FUNCTION_ARG_forward_value") {
        FUNCTION_ARG_forward_value = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_forward_value") = FUNCTION_ARG_forward_value
      }
      if (kv._1 == "speed") {
        speed = kv._2.asInstanceOf[Int]
        DEFAULT_logs("speed") = speed
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
      if (kv._1 == "xcord") {
        xcord = kv._2.asInstanceOf[Double]
        DEFAULT_logs("xcord") = xcord
      }
      if (kv._1 == "FUNCTION_ARG_fct_value") {
        FUNCTION_ARG_fct_value = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_ARG_fct_value") = FUNCTION_ARG_fct_value
      }
    }
  }
}
