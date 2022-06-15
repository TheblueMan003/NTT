package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
import scala.util.Random
@lift
class Patch extends Agent {
  var pcolor: Any       = 0
  def get_pcolor(): Any = pcolor
  def set_pcolor(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("pcolor") = DEFAULT_value
    pcolor = DEFAULT_value
  }
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
      if (kv._1 == "pcolor") {
        pcolor = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_ARG_tick_advance_value") {
        FUNCTION_ARG_tick_advance_value = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_tick") {
        FUNCTION_RETURN_tick = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "shape") {
        shape = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_tick_advance") {
        FUNCTION_RETURN_tick_advance = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "create_particles_n") {
        create_particles_n = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_random_ycor") {
        FUNCTION_RETURN_random_ycor = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_compute_forces") {
        FUNCTION_RETURN_compute_forces = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "size") {
        size = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_random_xcor") {
        FUNCTION_RETURN_random_xcor = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_create_particles") {
        FUNCTION_RETURN_create_particles = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_reset_ticks") {
        FUNCTION_RETURN_reset_ticks = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "pxcor") {
        pxcor = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "FUNCTION_RETURN_apply_forces") {
        FUNCTION_RETURN_apply_forces = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_default_setup") {
        FUNCTION_RETURN_default_setup = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "pycor") {
        pycor = kv._2.asInstanceOf[Int]
      }
    }
  }
  override def DEFAULT_UpdateFromWorker(dic: mutable.Map[String, Any]): Unit = {
    var values = dic.toList
    while (!values.isEmpty) {
      var kv = values.head
      values = values.tail
      if (kv._1 == "pcolor") {
        pcolor = kv._2.asInstanceOf[Any]
        DEFAULT_logs("pcolor") = pcolor
      }
      if (kv._1 == "FUNCTION_ARG_tick_advance_value") {
        FUNCTION_ARG_tick_advance_value = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_tick_advance_value") = FUNCTION_ARG_tick_advance_value
      }
      if (kv._1 == "FUNCTION_RETURN_tick") {
        FUNCTION_RETURN_tick = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_tick") = FUNCTION_RETURN_tick
      }
      if (kv._1 == "shape") {
        shape = kv._2.asInstanceOf[Any]
        DEFAULT_logs("shape") = shape
      }
      if (kv._1 == "FUNCTION_RETURN_tick_advance") {
        FUNCTION_RETURN_tick_advance = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_tick_advance") = FUNCTION_RETURN_tick_advance
      }
      if (kv._1 == "create_particles_n") {
        create_particles_n = kv._2.asInstanceOf[Double]
        DEFAULT_logs("create_particles_n") = create_particles_n
      }
      if (kv._1 == "FUNCTION_RETURN_random_ycor") {
        FUNCTION_RETURN_random_ycor = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_RETURN_random_ycor") = FUNCTION_RETURN_random_ycor
      }
      if (kv._1 == "FUNCTION_RETURN_compute_forces") {
        FUNCTION_RETURN_compute_forces = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_compute_forces") = FUNCTION_RETURN_compute_forces
      }
      if (kv._1 == "size") {
        size = kv._2.asInstanceOf[Double]
        DEFAULT_logs("size") = size
      }
      if (kv._1 == "FUNCTION_RETURN_random_xcor") {
        FUNCTION_RETURN_random_xcor = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_RETURN_random_xcor") = FUNCTION_RETURN_random_xcor
      }
      if (kv._1 == "FUNCTION_RETURN_create_particles") {
        FUNCTION_RETURN_create_particles = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_create_particles") = FUNCTION_RETURN_create_particles
      }
      if (kv._1 == "FUNCTION_RETURN_reset_ticks") {
        FUNCTION_RETURN_reset_ticks = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_reset_ticks") = FUNCTION_RETURN_reset_ticks
      }
      if (kv._1 == "pxcor") {
        pxcor = kv._2.asInstanceOf[Int]
        DEFAULT_logs("pxcor") = pxcor
      }
      if (kv._1 == "FUNCTION_RETURN_apply_forces") {
        FUNCTION_RETURN_apply_forces = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_apply_forces") = FUNCTION_RETURN_apply_forces
      }
      if (kv._1 == "FUNCTION_RETURN_default_setup") {
        FUNCTION_RETURN_default_setup = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_default_setup") = FUNCTION_RETURN_default_setup
      }
      if (kv._1 == "pycor") {
        pycor = kv._2.asInstanceOf[Int]
        DEFAULT_logs("pycor") = pycor
      }
    }
  }
}
