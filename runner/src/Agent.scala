package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
import scala.util.Random
@lift
class Agent extends Actor {
  val DEFAULT_logs                                  = mutable.Map[String, Any]()
  def get_DEFAULT_logs(): mutable.Map[String, Any]  = DEFAULT_logs
  var DEFAULT_ASK                                   = -1
  var DEFAULT_INITER                                = -1
  var DEFAULT_observer: Observer                    = null
  var DEFAULT_myself: Agent                         = null
  var DEFAULT_ASK_STACK: List[Agent]                = List()
  var FUNCTION_ARG_tick_advance_value: Double       = 0
  var FUNCTION_RETURN_tick: Any                     = 0
  var shape: Any                                    = 0
  var FUNCTION_RETURN_tick_advance: Any             = 0
  var create_particles_n: Double                    = 0
  var FUNCTION_RETURN_random_ycor: Double           = 0
  var FUNCTION_RETURN_compute_forces: Any           = 0
  var size: Double                                  = 0
  var FUNCTION_RETURN_random_xcor: Double           = 0
  var FUNCTION_RETURN_create_particles: Any         = 0
  var FUNCTION_RETURN_reset_ticks: Any              = 0
  var pxcor: Int                                    = 0
  var FUNCTION_RETURN_apply_forces: Any             = 0
  var FUNCTION_RETURN_default_setup: Any            = 0
  var pycor: Int                                    = 0
  def get_FUNCTION_ARG_tick_advance_value(): Double = FUNCTION_ARG_tick_advance_value
  def set_FUNCTION_ARG_tick_advance_value(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_tick_advance_value") = DEFAULT_value
    FUNCTION_ARG_tick_advance_value = DEFAULT_value
  }
  def get_FUNCTION_RETURN_tick(): Any = FUNCTION_RETURN_tick
  def set_FUNCTION_RETURN_tick(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_tick") = DEFAULT_value
    FUNCTION_RETURN_tick = DEFAULT_value
  }
  def get_shape(): Any = shape
  def set_shape(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("shape") = DEFAULT_value
    shape = DEFAULT_value
  }
  def get_FUNCTION_RETURN_tick_advance(): Any = FUNCTION_RETURN_tick_advance
  def set_FUNCTION_RETURN_tick_advance(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_tick_advance") = DEFAULT_value
    FUNCTION_RETURN_tick_advance = DEFAULT_value
  }
  def get_create_particles_n(): Double = create_particles_n
  def set_create_particles_n(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("create_particles_n") = DEFAULT_value
    create_particles_n = DEFAULT_value
  }
  def get_FUNCTION_RETURN_random_ycor(): Double = FUNCTION_RETURN_random_ycor
  def set_FUNCTION_RETURN_random_ycor(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_random_ycor") = DEFAULT_value
    FUNCTION_RETURN_random_ycor = DEFAULT_value
  }
  def get_FUNCTION_RETURN_compute_forces(): Any = FUNCTION_RETURN_compute_forces
  def set_FUNCTION_RETURN_compute_forces(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_compute_forces") = DEFAULT_value
    FUNCTION_RETURN_compute_forces = DEFAULT_value
  }
  def get_size(): Double = size
  def set_size(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("size") = DEFAULT_value
    size = DEFAULT_value
  }
  def get_FUNCTION_RETURN_random_xcor(): Double = FUNCTION_RETURN_random_xcor
  def set_FUNCTION_RETURN_random_xcor(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_random_xcor") = DEFAULT_value
    FUNCTION_RETURN_random_xcor = DEFAULT_value
  }
  def get_FUNCTION_RETURN_create_particles(): Any = FUNCTION_RETURN_create_particles
  def set_FUNCTION_RETURN_create_particles(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_create_particles") = DEFAULT_value
    FUNCTION_RETURN_create_particles = DEFAULT_value
  }
  def get_FUNCTION_RETURN_reset_ticks(): Any = FUNCTION_RETURN_reset_ticks
  def set_FUNCTION_RETURN_reset_ticks(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_reset_ticks") = DEFAULT_value
    FUNCTION_RETURN_reset_ticks = DEFAULT_value
  }
  def get_pxcor(): Int = pxcor
  def set_pxcor(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("pxcor") = DEFAULT_value
    pxcor = DEFAULT_value
  }
  def get_FUNCTION_RETURN_apply_forces(): Any = FUNCTION_RETURN_apply_forces
  def set_FUNCTION_RETURN_apply_forces(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_apply_forces") = DEFAULT_value
    FUNCTION_RETURN_apply_forces = DEFAULT_value
  }
  def get_FUNCTION_RETURN_default_setup(): Any = FUNCTION_RETURN_default_setup
  def set_FUNCTION_RETURN_default_setup(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_default_setup") = DEFAULT_value
    FUNCTION_RETURN_default_setup = DEFAULT_value
  }
  def get_pycor(): Int = pycor
  def set_pycor(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("pycor") = DEFAULT_value
    pycor = DEFAULT_value
  }
  def main(): Unit = {
    while (true) {
      handleMessages()
      waitLabel(Turn, 1)
    }
  }
  def DEFAULT_UpdateFromParent(dic: mutable.Map[String, Any]): Unit = {
    var values = dic.toList
    while (!values.isEmpty) {
      var kv = values.head
      values = values.tail
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
  def DEFAULT_UpdateFromWorker(dic: mutable.Map[String, Any]): Unit = {
    var values = dic.toList
    while (!values.isEmpty) {
      var kv = values.head
      values = values.tail
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
