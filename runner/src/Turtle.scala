package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
import scala.util.Random
@lift
class Turtle extends Agent {
  var FUNCTION_RETURN_default_init_turtle: Any       = 0
  var face_y: Double                                 = 0
  var face_x: Double                                 = 0
  var FUNCTION_ARG_forward_value: Double             = 0
  var velocity_x: Double                             = 0
  var velocity_y: Double                             = 0
  var FUNCTION_RETURN_forward: Any                   = 0
  var FUNCTION_RETURN_apply_wind: Any                = 0
  var visible: Boolean                               = false
  var FUNCTION_ARG_can_move__value: Any              = 0
  var FUNCTION_ARG_move_to_a: Any                    = 0
  var FUNCTION_RETURN_lambda_29: Double              = 0
  var FUNCTION_RETURN_left: Any                      = 0
  var FUNCTION_RETURN_setxy: Any                     = 0
  var distancexy_d: Double                           = 0
  var FUNCTION_RETURN_dy: Double                     = 0
  var FUNCTION_RETURN_dx: Double                     = 0
  var lambda_27_new_y: Double                        = 0
  var xcor: Double                                   = 0
  var lambda_27_new_x: Double                        = 0
  var FUNCTION_ARG_facexy_x: Double                  = 0
  var FUNCTION_ARG_facexy_y: Double                  = 0
  var FUNCTION_RETURN_distance: Double               = 0
  var FUNCTION_RETURN_lt: Any                        = 0
  var FUNCTION_ARG_distance_value: Any               = 0
  var FUNCTION_RETURN_hide_turtle: Any               = 0
  var FUNCTION_ARG_rt_value: Double                  = 0
  var FUNCTION_RETURN_apply_gravity: Any             = 0
  var FUNCTION_ARG_right_value: Double               = 0
  var color: Int                                     = 0
  var FUNCTION_RETURN_apply_viscosity: Any           = 0
  var FUNCTION_ARG_distancexy_x: Double              = 0
  var mass: Double                                   = 0
  var FUNCTION_RETURN_distancexy: Double             = 0
  var FUNCTION_RETURN_right: Any                     = 0
  var distancexy_dx: Double                          = 0
  var FUNCTION_RETURN_lambda_33: Double              = 0
  var distancexy_dy: Double                          = 0
  var FUNCTION_RETURN_lambda_35: Double              = 0
  var shape_size: Int                                = 0
  var FUNCTION_RETURN_lambda_37: Double              = 0
  var FUNCTION_RETURN_lambda_39: Double              = 0
  var FUNCTION_ARG_fd_value: Double                  = 0
  var ycor: Double                                   = 0
  var FUNCTION_RETURN_lambda_31: Double              = 0
  var FUNCTION_ARG_setxy_y: Double                   = 0
  var FUNCTION_ARG_setxy_x: Double                   = 0
  var FUNCTION_ARG_left_value: Double                = 0
  var FUNCTION_RETURN_home: Any                      = 0
  var FUNCTION_RETURN_face: Any                      = 0
  var FUNCTION_RETURN_default_init: Any              = 0
  var FUNCTION_RETURN_rt: Any                        = 0
  var FUNCTION_RETURN_can_move_ : Boolean            = false
  var heading: Double                                = 0
  var FUNCTION_RETURN_facexy: Any                    = 0
  var force_accumulator_x: Double                    = 0
  var FUNCTION_RETURN_fd: Any                        = 0
  var force_accumulator_y: Double                    = 0
  var FUNCTION_RETURN_move_to: Any                   = 0
  var FUNCTION_ARG_lt_value: Double                  = 0
  var FUNCTION_RETURN_show_turtle: Any               = 0
  var lambda_27_step_y: Double                       = 0
  var lambda_27_step_x: Double                       = 0
  var FUNCTION_ARG_face_value: Any                   = 0
  var FUNCTION_ARG_distancexy_y: Double              = 0
  var forward_m: Double                              = 0
  def get_FUNCTION_RETURN_default_init_turtle(): Any = FUNCTION_RETURN_default_init_turtle
  def set_FUNCTION_RETURN_default_init_turtle(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_default_init_turtle") = DEFAULT_value
    FUNCTION_RETURN_default_init_turtle = DEFAULT_value
  }
  def get_face_y(): Double = face_y
  def set_face_y(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("face_y") = DEFAULT_value
    face_y = DEFAULT_value
  }
  def get_face_x(): Double = face_x
  def set_face_x(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("face_x") = DEFAULT_value
    face_x = DEFAULT_value
  }
  def get_FUNCTION_ARG_forward_value(): Double = FUNCTION_ARG_forward_value
  def set_FUNCTION_ARG_forward_value(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_forward_value") = DEFAULT_value
    FUNCTION_ARG_forward_value = DEFAULT_value
  }
  def get_velocity_x(): Double = velocity_x
  def set_velocity_x(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("velocity_x") = DEFAULT_value
    velocity_x = DEFAULT_value
  }
  def get_velocity_y(): Double = velocity_y
  def set_velocity_y(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("velocity_y") = DEFAULT_value
    velocity_y = DEFAULT_value
  }
  def get_FUNCTION_RETURN_forward(): Any = FUNCTION_RETURN_forward
  def set_FUNCTION_RETURN_forward(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_forward") = DEFAULT_value
    FUNCTION_RETURN_forward = DEFAULT_value
  }
  def get_FUNCTION_RETURN_apply_wind(): Any = FUNCTION_RETURN_apply_wind
  def set_FUNCTION_RETURN_apply_wind(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_apply_wind") = DEFAULT_value
    FUNCTION_RETURN_apply_wind = DEFAULT_value
  }
  def get_visible(): Boolean = visible
  def set_visible(DEFAULT_value: Boolean): Unit = {
    DEFAULT_logs("visible") = DEFAULT_value
    visible = DEFAULT_value
  }
  def get_FUNCTION_ARG_can_move__value(): Any = FUNCTION_ARG_can_move__value
  def set_FUNCTION_ARG_can_move__value(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_ARG_can_move__value") = DEFAULT_value
    FUNCTION_ARG_can_move__value = DEFAULT_value
  }
  def get_FUNCTION_ARG_move_to_a(): Any = FUNCTION_ARG_move_to_a
  def set_FUNCTION_ARG_move_to_a(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_ARG_move_to_a") = DEFAULT_value
    FUNCTION_ARG_move_to_a = DEFAULT_value
  }
  def get_FUNCTION_RETURN_lambda_29(): Double = FUNCTION_RETURN_lambda_29
  def set_FUNCTION_RETURN_lambda_29(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_lambda_29") = DEFAULT_value
    FUNCTION_RETURN_lambda_29 = DEFAULT_value
  }
  def get_FUNCTION_RETURN_left(): Any = FUNCTION_RETURN_left
  def set_FUNCTION_RETURN_left(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_left") = DEFAULT_value
    FUNCTION_RETURN_left = DEFAULT_value
  }
  def get_FUNCTION_RETURN_setxy(): Any = FUNCTION_RETURN_setxy
  def set_FUNCTION_RETURN_setxy(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_setxy") = DEFAULT_value
    FUNCTION_RETURN_setxy = DEFAULT_value
  }
  def get_distancexy_d(): Double = distancexy_d
  def set_distancexy_d(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("distancexy_d") = DEFAULT_value
    distancexy_d = DEFAULT_value
  }
  def get_FUNCTION_RETURN_dy(): Double = FUNCTION_RETURN_dy
  def set_FUNCTION_RETURN_dy(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_dy") = DEFAULT_value
    FUNCTION_RETURN_dy = DEFAULT_value
  }
  def get_FUNCTION_RETURN_dx(): Double = FUNCTION_RETURN_dx
  def set_FUNCTION_RETURN_dx(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_dx") = DEFAULT_value
    FUNCTION_RETURN_dx = DEFAULT_value
  }
  def get_lambda_27_new_y(): Double = lambda_27_new_y
  def set_lambda_27_new_y(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("lambda_27_new_y") = DEFAULT_value
    lambda_27_new_y = DEFAULT_value
  }
  def get_xcor(): Double = xcor
  def set_xcor(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("xcor") = DEFAULT_value
    xcor = DEFAULT_value
  }
  def get_lambda_27_new_x(): Double = lambda_27_new_x
  def set_lambda_27_new_x(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("lambda_27_new_x") = DEFAULT_value
    lambda_27_new_x = DEFAULT_value
  }
  def get_FUNCTION_ARG_facexy_x(): Double = FUNCTION_ARG_facexy_x
  def set_FUNCTION_ARG_facexy_x(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_facexy_x") = DEFAULT_value
    FUNCTION_ARG_facexy_x = DEFAULT_value
  }
  def get_FUNCTION_ARG_facexy_y(): Double = FUNCTION_ARG_facexy_y
  def set_FUNCTION_ARG_facexy_y(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_facexy_y") = DEFAULT_value
    FUNCTION_ARG_facexy_y = DEFAULT_value
  }
  def get_FUNCTION_RETURN_distance(): Double = FUNCTION_RETURN_distance
  def set_FUNCTION_RETURN_distance(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_distance") = DEFAULT_value
    FUNCTION_RETURN_distance = DEFAULT_value
  }
  def get_FUNCTION_RETURN_lt(): Any = FUNCTION_RETURN_lt
  def set_FUNCTION_RETURN_lt(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_lt") = DEFAULT_value
    FUNCTION_RETURN_lt = DEFAULT_value
  }
  def get_FUNCTION_ARG_distance_value(): Any = FUNCTION_ARG_distance_value
  def set_FUNCTION_ARG_distance_value(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_ARG_distance_value") = DEFAULT_value
    FUNCTION_ARG_distance_value = DEFAULT_value
  }
  def get_FUNCTION_RETURN_hide_turtle(): Any = FUNCTION_RETURN_hide_turtle
  def set_FUNCTION_RETURN_hide_turtle(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_hide_turtle") = DEFAULT_value
    FUNCTION_RETURN_hide_turtle = DEFAULT_value
  }
  def get_FUNCTION_ARG_rt_value(): Double = FUNCTION_ARG_rt_value
  def set_FUNCTION_ARG_rt_value(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_rt_value") = DEFAULT_value
    FUNCTION_ARG_rt_value = DEFAULT_value
  }
  def get_FUNCTION_RETURN_apply_gravity(): Any = FUNCTION_RETURN_apply_gravity
  def set_FUNCTION_RETURN_apply_gravity(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_apply_gravity") = DEFAULT_value
    FUNCTION_RETURN_apply_gravity = DEFAULT_value
  }
  def get_FUNCTION_ARG_right_value(): Double = FUNCTION_ARG_right_value
  def set_FUNCTION_ARG_right_value(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_right_value") = DEFAULT_value
    FUNCTION_ARG_right_value = DEFAULT_value
  }
  def get_color(): Int = color
  def set_color(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("color") = DEFAULT_value
    color = DEFAULT_value
  }
  def get_FUNCTION_RETURN_apply_viscosity(): Any = FUNCTION_RETURN_apply_viscosity
  def set_FUNCTION_RETURN_apply_viscosity(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_apply_viscosity") = DEFAULT_value
    FUNCTION_RETURN_apply_viscosity = DEFAULT_value
  }
  def get_FUNCTION_ARG_distancexy_x(): Double = FUNCTION_ARG_distancexy_x
  def set_FUNCTION_ARG_distancexy_x(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_distancexy_x") = DEFAULT_value
    FUNCTION_ARG_distancexy_x = DEFAULT_value
  }
  def get_mass(): Double = mass
  def set_mass(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("mass") = DEFAULT_value
    mass = DEFAULT_value
  }
  def get_FUNCTION_RETURN_distancexy(): Double = FUNCTION_RETURN_distancexy
  def set_FUNCTION_RETURN_distancexy(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_distancexy") = DEFAULT_value
    FUNCTION_RETURN_distancexy = DEFAULT_value
  }
  def get_FUNCTION_RETURN_right(): Any = FUNCTION_RETURN_right
  def set_FUNCTION_RETURN_right(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_right") = DEFAULT_value
    FUNCTION_RETURN_right = DEFAULT_value
  }
  def get_distancexy_dx(): Double = distancexy_dx
  def set_distancexy_dx(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("distancexy_dx") = DEFAULT_value
    distancexy_dx = DEFAULT_value
  }
  def get_FUNCTION_RETURN_lambda_33(): Double = FUNCTION_RETURN_lambda_33
  def set_FUNCTION_RETURN_lambda_33(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_lambda_33") = DEFAULT_value
    FUNCTION_RETURN_lambda_33 = DEFAULT_value
  }
  def get_distancexy_dy(): Double = distancexy_dy
  def set_distancexy_dy(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("distancexy_dy") = DEFAULT_value
    distancexy_dy = DEFAULT_value
  }
  def get_FUNCTION_RETURN_lambda_35(): Double = FUNCTION_RETURN_lambda_35
  def set_FUNCTION_RETURN_lambda_35(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_lambda_35") = DEFAULT_value
    FUNCTION_RETURN_lambda_35 = DEFAULT_value
  }
  def get_shape_size(): Int = shape_size
  def set_shape_size(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("shape_size") = DEFAULT_value
    shape_size = DEFAULT_value
  }
  def get_FUNCTION_RETURN_lambda_37(): Double = FUNCTION_RETURN_lambda_37
  def set_FUNCTION_RETURN_lambda_37(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_lambda_37") = DEFAULT_value
    FUNCTION_RETURN_lambda_37 = DEFAULT_value
  }
  def get_FUNCTION_RETURN_lambda_39(): Double = FUNCTION_RETURN_lambda_39
  def set_FUNCTION_RETURN_lambda_39(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_lambda_39") = DEFAULT_value
    FUNCTION_RETURN_lambda_39 = DEFAULT_value
  }
  def get_FUNCTION_ARG_fd_value(): Double = FUNCTION_ARG_fd_value
  def set_FUNCTION_ARG_fd_value(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_fd_value") = DEFAULT_value
    FUNCTION_ARG_fd_value = DEFAULT_value
  }
  def get_ycor(): Double = ycor
  def set_ycor(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("ycor") = DEFAULT_value
    ycor = DEFAULT_value
  }
  def get_FUNCTION_RETURN_lambda_31(): Double = FUNCTION_RETURN_lambda_31
  def set_FUNCTION_RETURN_lambda_31(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_lambda_31") = DEFAULT_value
    FUNCTION_RETURN_lambda_31 = DEFAULT_value
  }
  def get_FUNCTION_ARG_setxy_y(): Double = FUNCTION_ARG_setxy_y
  def set_FUNCTION_ARG_setxy_y(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_setxy_y") = DEFAULT_value
    FUNCTION_ARG_setxy_y = DEFAULT_value
  }
  def get_FUNCTION_ARG_setxy_x(): Double = FUNCTION_ARG_setxy_x
  def set_FUNCTION_ARG_setxy_x(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_setxy_x") = DEFAULT_value
    FUNCTION_ARG_setxy_x = DEFAULT_value
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
  def get_FUNCTION_RETURN_face(): Any = FUNCTION_RETURN_face
  def set_FUNCTION_RETURN_face(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_face") = DEFAULT_value
    FUNCTION_RETURN_face = DEFAULT_value
  }
  def get_FUNCTION_RETURN_default_init(): Any = FUNCTION_RETURN_default_init
  def set_FUNCTION_RETURN_default_init(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_default_init") = DEFAULT_value
    FUNCTION_RETURN_default_init = DEFAULT_value
  }
  def get_FUNCTION_RETURN_rt(): Any = FUNCTION_RETURN_rt
  def set_FUNCTION_RETURN_rt(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_rt") = DEFAULT_value
    FUNCTION_RETURN_rt = DEFAULT_value
  }
  def get_FUNCTION_RETURN_can_move_(): Boolean = FUNCTION_RETURN_can_move_
  def set_FUNCTION_RETURN_can_move_(DEFAULT_value: Boolean): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_can_move_") = DEFAULT_value
    FUNCTION_RETURN_can_move_ = DEFAULT_value
  }
  def get_heading(): Double = heading
  def set_heading(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("heading") = DEFAULT_value
    heading = DEFAULT_value
  }
  def get_FUNCTION_RETURN_facexy(): Any = FUNCTION_RETURN_facexy
  def set_FUNCTION_RETURN_facexy(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_facexy") = DEFAULT_value
    FUNCTION_RETURN_facexy = DEFAULT_value
  }
  def get_force_accumulator_x(): Double = force_accumulator_x
  def set_force_accumulator_x(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("force_accumulator_x") = DEFAULT_value
    force_accumulator_x = DEFAULT_value
  }
  def get_FUNCTION_RETURN_fd(): Any = FUNCTION_RETURN_fd
  def set_FUNCTION_RETURN_fd(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_fd") = DEFAULT_value
    FUNCTION_RETURN_fd = DEFAULT_value
  }
  def get_force_accumulator_y(): Double = force_accumulator_y
  def set_force_accumulator_y(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("force_accumulator_y") = DEFAULT_value
    force_accumulator_y = DEFAULT_value
  }
  def get_FUNCTION_RETURN_move_to(): Any = FUNCTION_RETURN_move_to
  def set_FUNCTION_RETURN_move_to(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_move_to") = DEFAULT_value
    FUNCTION_RETURN_move_to = DEFAULT_value
  }
  def get_FUNCTION_ARG_lt_value(): Double = FUNCTION_ARG_lt_value
  def set_FUNCTION_ARG_lt_value(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_lt_value") = DEFAULT_value
    FUNCTION_ARG_lt_value = DEFAULT_value
  }
  def get_FUNCTION_RETURN_show_turtle(): Any = FUNCTION_RETURN_show_turtle
  def set_FUNCTION_RETURN_show_turtle(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_show_turtle") = DEFAULT_value
    FUNCTION_RETURN_show_turtle = DEFAULT_value
  }
  def get_lambda_27_step_y(): Double = lambda_27_step_y
  def set_lambda_27_step_y(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("lambda_27_step_y") = DEFAULT_value
    lambda_27_step_y = DEFAULT_value
  }
  def get_lambda_27_step_x(): Double = lambda_27_step_x
  def set_lambda_27_step_x(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("lambda_27_step_x") = DEFAULT_value
    lambda_27_step_x = DEFAULT_value
  }
  def get_FUNCTION_ARG_face_value(): Any = FUNCTION_ARG_face_value
  def set_FUNCTION_ARG_face_value(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_ARG_face_value") = DEFAULT_value
    FUNCTION_ARG_face_value = DEFAULT_value
  }
  def get_FUNCTION_ARG_distancexy_y(): Double = FUNCTION_ARG_distancexy_y
  def set_FUNCTION_ARG_distancexy_y(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("FUNCTION_ARG_distancexy_y") = DEFAULT_value
    FUNCTION_ARG_distancexy_y = DEFAULT_value
  }
  def get_forward_m(): Double = forward_m
  def set_forward_m(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("forward_m") = DEFAULT_value
    forward_m = DEFAULT_value
  }
  override def main(): Unit = {
    markOverride("DEFAULT_UpdateFromParent", "DEFAULT_UpdateFromWorker")
    // lambda_25
    if (DEFAULT_INITER == 26) {
      {
        set_color(DEFAULT_observer.get_blue())
        set_size(0.1 + Random.nextFloat)
        set_mass(Math.pow(get_size(), 2))
        set_velocity_x(
          DEFAULT_observer.get_initial_velocity_x() - Random.nextFloat + Random.nextFloat
        )
        set_velocity_y(DEFAULT_observer.get_initial_velocity_y())
      }
    }
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
      if (kv._1 == "FUNCTION_RETURN_default_init_turtle") {
        FUNCTION_RETURN_default_init_turtle = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "face_y") {
        face_y = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "face_x") {
        face_x = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_ARG_forward_value") {
        FUNCTION_ARG_forward_value = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "velocity_x") {
        velocity_x = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "velocity_y") {
        velocity_y = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_forward") {
        FUNCTION_RETURN_forward = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_apply_wind") {
        FUNCTION_RETURN_apply_wind = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "visible") {
        visible = kv._2.asInstanceOf[Boolean]
      }
      if (kv._1 == "FUNCTION_ARG_can_move__value") {
        FUNCTION_ARG_can_move__value = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_ARG_move_to_a") {
        FUNCTION_ARG_move_to_a = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_lambda_29") {
        FUNCTION_RETURN_lambda_29 = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_left") {
        FUNCTION_RETURN_left = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_setxy") {
        FUNCTION_RETURN_setxy = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "distancexy_d") {
        distancexy_d = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_dy") {
        FUNCTION_RETURN_dy = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_dx") {
        FUNCTION_RETURN_dx = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "lambda_27_new_y") {
        lambda_27_new_y = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "xcor") {
        xcor = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "lambda_27_new_x") {
        lambda_27_new_x = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_ARG_facexy_x") {
        FUNCTION_ARG_facexy_x = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_ARG_facexy_y") {
        FUNCTION_ARG_facexy_y = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_distance") {
        FUNCTION_RETURN_distance = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_lt") {
        FUNCTION_RETURN_lt = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_ARG_distance_value") {
        FUNCTION_ARG_distance_value = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_hide_turtle") {
        FUNCTION_RETURN_hide_turtle = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_ARG_rt_value") {
        FUNCTION_ARG_rt_value = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_apply_gravity") {
        FUNCTION_RETURN_apply_gravity = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_ARG_right_value") {
        FUNCTION_ARG_right_value = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "color") {
        color = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "FUNCTION_RETURN_apply_viscosity") {
        FUNCTION_RETURN_apply_viscosity = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_ARG_distancexy_x") {
        FUNCTION_ARG_distancexy_x = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "mass") {
        mass = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_distancexy") {
        FUNCTION_RETURN_distancexy = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_right") {
        FUNCTION_RETURN_right = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "distancexy_dx") {
        distancexy_dx = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_lambda_33") {
        FUNCTION_RETURN_lambda_33 = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "distancexy_dy") {
        distancexy_dy = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_lambda_35") {
        FUNCTION_RETURN_lambda_35 = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "shape_size") {
        shape_size = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "FUNCTION_RETURN_lambda_37") {
        FUNCTION_RETURN_lambda_37 = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_lambda_39") {
        FUNCTION_RETURN_lambda_39 = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_ARG_fd_value") {
        FUNCTION_ARG_fd_value = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "ycor") {
        ycor = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_lambda_31") {
        FUNCTION_RETURN_lambda_31 = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_ARG_setxy_y") {
        FUNCTION_ARG_setxy_y = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_ARG_setxy_x") {
        FUNCTION_ARG_setxy_x = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_ARG_left_value") {
        FUNCTION_ARG_left_value = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_home") {
        FUNCTION_RETURN_home = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_face") {
        FUNCTION_RETURN_face = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_default_init") {
        FUNCTION_RETURN_default_init = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_rt") {
        FUNCTION_RETURN_rt = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_can_move_") {
        FUNCTION_RETURN_can_move_ = kv._2.asInstanceOf[Boolean]
      }
      if (kv._1 == "heading") {
        heading = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_facexy") {
        FUNCTION_RETURN_facexy = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "force_accumulator_x") {
        force_accumulator_x = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_fd") {
        FUNCTION_RETURN_fd = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "force_accumulator_y") {
        force_accumulator_y = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_move_to") {
        FUNCTION_RETURN_move_to = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_ARG_lt_value") {
        FUNCTION_ARG_lt_value = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_show_turtle") {
        FUNCTION_RETURN_show_turtle = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "lambda_27_step_y") {
        lambda_27_step_y = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "lambda_27_step_x") {
        lambda_27_step_x = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_ARG_face_value") {
        FUNCTION_ARG_face_value = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_ARG_distancexy_y") {
        FUNCTION_ARG_distancexy_y = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "forward_m") {
        forward_m = kv._2.asInstanceOf[Double]
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
      if (kv._1 == "FUNCTION_RETURN_default_init_turtle") {
        FUNCTION_RETURN_default_init_turtle = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_default_init_turtle") = FUNCTION_RETURN_default_init_turtle
      }
      if (kv._1 == "face_y") {
        face_y = kv._2.asInstanceOf[Double]
        DEFAULT_logs("face_y") = face_y
      }
      if (kv._1 == "face_x") {
        face_x = kv._2.asInstanceOf[Double]
        DEFAULT_logs("face_x") = face_x
      }
      if (kv._1 == "FUNCTION_ARG_forward_value") {
        FUNCTION_ARG_forward_value = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_forward_value") = FUNCTION_ARG_forward_value
      }
      if (kv._1 == "velocity_x") {
        velocity_x = kv._2.asInstanceOf[Double]
        DEFAULT_logs("velocity_x") = velocity_x
      }
      if (kv._1 == "velocity_y") {
        velocity_y = kv._2.asInstanceOf[Double]
        DEFAULT_logs("velocity_y") = velocity_y
      }
      if (kv._1 == "FUNCTION_RETURN_forward") {
        FUNCTION_RETURN_forward = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_forward") = FUNCTION_RETURN_forward
      }
      if (kv._1 == "FUNCTION_RETURN_apply_wind") {
        FUNCTION_RETURN_apply_wind = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_apply_wind") = FUNCTION_RETURN_apply_wind
      }
      if (kv._1 == "visible") {
        visible = kv._2.asInstanceOf[Boolean]
        DEFAULT_logs("visible") = visible
      }
      if (kv._1 == "FUNCTION_ARG_can_move__value") {
        FUNCTION_ARG_can_move__value = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_ARG_can_move__value") = FUNCTION_ARG_can_move__value
      }
      if (kv._1 == "FUNCTION_ARG_move_to_a") {
        FUNCTION_ARG_move_to_a = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_ARG_move_to_a") = FUNCTION_ARG_move_to_a
      }
      if (kv._1 == "FUNCTION_RETURN_lambda_29") {
        FUNCTION_RETURN_lambda_29 = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_RETURN_lambda_29") = FUNCTION_RETURN_lambda_29
      }
      if (kv._1 == "FUNCTION_RETURN_left") {
        FUNCTION_RETURN_left = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_left") = FUNCTION_RETURN_left
      }
      if (kv._1 == "FUNCTION_RETURN_setxy") {
        FUNCTION_RETURN_setxy = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_setxy") = FUNCTION_RETURN_setxy
      }
      if (kv._1 == "distancexy_d") {
        distancexy_d = kv._2.asInstanceOf[Double]
        DEFAULT_logs("distancexy_d") = distancexy_d
      }
      if (kv._1 == "FUNCTION_RETURN_dy") {
        FUNCTION_RETURN_dy = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_RETURN_dy") = FUNCTION_RETURN_dy
      }
      if (kv._1 == "FUNCTION_RETURN_dx") {
        FUNCTION_RETURN_dx = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_RETURN_dx") = FUNCTION_RETURN_dx
      }
      if (kv._1 == "lambda_27_new_y") {
        lambda_27_new_y = kv._2.asInstanceOf[Double]
        DEFAULT_logs("lambda_27_new_y") = lambda_27_new_y
      }
      if (kv._1 == "xcor") {
        xcor = kv._2.asInstanceOf[Double]
        DEFAULT_logs("xcor") = xcor
      }
      if (kv._1 == "lambda_27_new_x") {
        lambda_27_new_x = kv._2.asInstanceOf[Double]
        DEFAULT_logs("lambda_27_new_x") = lambda_27_new_x
      }
      if (kv._1 == "FUNCTION_ARG_facexy_x") {
        FUNCTION_ARG_facexy_x = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_facexy_x") = FUNCTION_ARG_facexy_x
      }
      if (kv._1 == "FUNCTION_ARG_facexy_y") {
        FUNCTION_ARG_facexy_y = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_facexy_y") = FUNCTION_ARG_facexy_y
      }
      if (kv._1 == "FUNCTION_RETURN_distance") {
        FUNCTION_RETURN_distance = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_RETURN_distance") = FUNCTION_RETURN_distance
      }
      if (kv._1 == "FUNCTION_RETURN_lt") {
        FUNCTION_RETURN_lt = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_lt") = FUNCTION_RETURN_lt
      }
      if (kv._1 == "FUNCTION_ARG_distance_value") {
        FUNCTION_ARG_distance_value = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_ARG_distance_value") = FUNCTION_ARG_distance_value
      }
      if (kv._1 == "FUNCTION_RETURN_hide_turtle") {
        FUNCTION_RETURN_hide_turtle = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_hide_turtle") = FUNCTION_RETURN_hide_turtle
      }
      if (kv._1 == "FUNCTION_ARG_rt_value") {
        FUNCTION_ARG_rt_value = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_rt_value") = FUNCTION_ARG_rt_value
      }
      if (kv._1 == "FUNCTION_RETURN_apply_gravity") {
        FUNCTION_RETURN_apply_gravity = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_apply_gravity") = FUNCTION_RETURN_apply_gravity
      }
      if (kv._1 == "FUNCTION_ARG_right_value") {
        FUNCTION_ARG_right_value = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_right_value") = FUNCTION_ARG_right_value
      }
      if (kv._1 == "color") {
        color = kv._2.asInstanceOf[Int]
        DEFAULT_logs("color") = color
      }
      if (kv._1 == "FUNCTION_RETURN_apply_viscosity") {
        FUNCTION_RETURN_apply_viscosity = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_apply_viscosity") = FUNCTION_RETURN_apply_viscosity
      }
      if (kv._1 == "FUNCTION_ARG_distancexy_x") {
        FUNCTION_ARG_distancexy_x = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_distancexy_x") = FUNCTION_ARG_distancexy_x
      }
      if (kv._1 == "mass") {
        mass = kv._2.asInstanceOf[Double]
        DEFAULT_logs("mass") = mass
      }
      if (kv._1 == "FUNCTION_RETURN_distancexy") {
        FUNCTION_RETURN_distancexy = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_RETURN_distancexy") = FUNCTION_RETURN_distancexy
      }
      if (kv._1 == "FUNCTION_RETURN_right") {
        FUNCTION_RETURN_right = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_right") = FUNCTION_RETURN_right
      }
      if (kv._1 == "distancexy_dx") {
        distancexy_dx = kv._2.asInstanceOf[Double]
        DEFAULT_logs("distancexy_dx") = distancexy_dx
      }
      if (kv._1 == "FUNCTION_RETURN_lambda_33") {
        FUNCTION_RETURN_lambda_33 = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_RETURN_lambda_33") = FUNCTION_RETURN_lambda_33
      }
      if (kv._1 == "distancexy_dy") {
        distancexy_dy = kv._2.asInstanceOf[Double]
        DEFAULT_logs("distancexy_dy") = distancexy_dy
      }
      if (kv._1 == "FUNCTION_RETURN_lambda_35") {
        FUNCTION_RETURN_lambda_35 = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_RETURN_lambda_35") = FUNCTION_RETURN_lambda_35
      }
      if (kv._1 == "shape_size") {
        shape_size = kv._2.asInstanceOf[Int]
        DEFAULT_logs("shape_size") = shape_size
      }
      if (kv._1 == "FUNCTION_RETURN_lambda_37") {
        FUNCTION_RETURN_lambda_37 = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_RETURN_lambda_37") = FUNCTION_RETURN_lambda_37
      }
      if (kv._1 == "FUNCTION_RETURN_lambda_39") {
        FUNCTION_RETURN_lambda_39 = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_RETURN_lambda_39") = FUNCTION_RETURN_lambda_39
      }
      if (kv._1 == "FUNCTION_ARG_fd_value") {
        FUNCTION_ARG_fd_value = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_fd_value") = FUNCTION_ARG_fd_value
      }
      if (kv._1 == "ycor") {
        ycor = kv._2.asInstanceOf[Double]
        DEFAULT_logs("ycor") = ycor
      }
      if (kv._1 == "FUNCTION_RETURN_lambda_31") {
        FUNCTION_RETURN_lambda_31 = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_RETURN_lambda_31") = FUNCTION_RETURN_lambda_31
      }
      if (kv._1 == "FUNCTION_ARG_setxy_y") {
        FUNCTION_ARG_setxy_y = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_setxy_y") = FUNCTION_ARG_setxy_y
      }
      if (kv._1 == "FUNCTION_ARG_setxy_x") {
        FUNCTION_ARG_setxy_x = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_setxy_x") = FUNCTION_ARG_setxy_x
      }
      if (kv._1 == "FUNCTION_ARG_left_value") {
        FUNCTION_ARG_left_value = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_left_value") = FUNCTION_ARG_left_value
      }
      if (kv._1 == "FUNCTION_RETURN_home") {
        FUNCTION_RETURN_home = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_home") = FUNCTION_RETURN_home
      }
      if (kv._1 == "FUNCTION_RETURN_face") {
        FUNCTION_RETURN_face = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_face") = FUNCTION_RETURN_face
      }
      if (kv._1 == "FUNCTION_RETURN_default_init") {
        FUNCTION_RETURN_default_init = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_default_init") = FUNCTION_RETURN_default_init
      }
      if (kv._1 == "FUNCTION_RETURN_rt") {
        FUNCTION_RETURN_rt = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_rt") = FUNCTION_RETURN_rt
      }
      if (kv._1 == "FUNCTION_RETURN_can_move_") {
        FUNCTION_RETURN_can_move_ = kv._2.asInstanceOf[Boolean]
        DEFAULT_logs("FUNCTION_RETURN_can_move_") = FUNCTION_RETURN_can_move_
      }
      if (kv._1 == "heading") {
        heading = kv._2.asInstanceOf[Double]
        DEFAULT_logs("heading") = heading
      }
      if (kv._1 == "FUNCTION_RETURN_facexy") {
        FUNCTION_RETURN_facexy = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_facexy") = FUNCTION_RETURN_facexy
      }
      if (kv._1 == "force_accumulator_x") {
        force_accumulator_x = kv._2.asInstanceOf[Double]
        DEFAULT_logs("force_accumulator_x") = force_accumulator_x
      }
      if (kv._1 == "FUNCTION_RETURN_fd") {
        FUNCTION_RETURN_fd = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_fd") = FUNCTION_RETURN_fd
      }
      if (kv._1 == "force_accumulator_y") {
        force_accumulator_y = kv._2.asInstanceOf[Double]
        DEFAULT_logs("force_accumulator_y") = force_accumulator_y
      }
      if (kv._1 == "FUNCTION_RETURN_move_to") {
        FUNCTION_RETURN_move_to = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_move_to") = FUNCTION_RETURN_move_to
      }
      if (kv._1 == "FUNCTION_ARG_lt_value") {
        FUNCTION_ARG_lt_value = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_lt_value") = FUNCTION_ARG_lt_value
      }
      if (kv._1 == "FUNCTION_RETURN_show_turtle") {
        FUNCTION_RETURN_show_turtle = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_show_turtle") = FUNCTION_RETURN_show_turtle
      }
      if (kv._1 == "lambda_27_step_y") {
        lambda_27_step_y = kv._2.asInstanceOf[Double]
        DEFAULT_logs("lambda_27_step_y") = lambda_27_step_y
      }
      if (kv._1 == "lambda_27_step_x") {
        lambda_27_step_x = kv._2.asInstanceOf[Double]
        DEFAULT_logs("lambda_27_step_x") = lambda_27_step_x
      }
      if (kv._1 == "FUNCTION_ARG_face_value") {
        FUNCTION_ARG_face_value = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_ARG_face_value") = FUNCTION_ARG_face_value
      }
      if (kv._1 == "FUNCTION_ARG_distancexy_y") {
        FUNCTION_ARG_distancexy_y = kv._2.asInstanceOf[Double]
        DEFAULT_logs("FUNCTION_ARG_distancexy_y") = FUNCTION_ARG_distancexy_y
      }
      if (kv._1 == "forward_m") {
        forward_m = kv._2.asInstanceOf[Double]
        DEFAULT_logs("forward_m") = forward_m
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
