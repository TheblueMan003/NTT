package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
import scala.util.Random
@lift
class Observer extends Agent {
  val agents                                 = mutable.Set[Agent]()
  def get_agents(): mutable.Set[Agent]       = agents
  val turtles                                = mutable.Set[Turtle]()
  def get_turtles(): mutable.Set[Turtle]     = turtles
  val patches                                = mutable.Set[Patch]()
  def get_patches(): mutable.Set[Patch]      = patches
  val observers                              = mutable.Set[Observer]()
  def get_observers(): mutable.Set[Observer] = observers
  val linkes                                 = mutable.Set[Link]()
  def get_linkes(): mutable.Set[Link]        = linkes
  var DEFAULT_BOARD_SIZE_X                   = 10
  var DEFAULT_BOARD_SIZE_Y                   = 10
  var magenta: Int                           = 0
  var initial_velocity_x: Int                = 0
  var pink: Int                              = 0
  var initial_velocity_y: Int                = 0
  var yellow: Int                            = 0
  var step_size: Double                      = 0
  var turquoise: Int                         = 0
  var max_number_of_particles: Int           = 0
  var cyan: Int                              = 0
  var red: Int                               = 0
  var gray: Int                              = 0
  var white: Double                          = 0
  var rate: Double                           = 0
  var max_pxcor: Double                      = 0
  var FUNCTION_RETURN_go: Any                = 0
  var min_pxcor: Double                      = 0
  var FUNCTION_RETURN_setup: Any             = 0
  var sky: Int                               = 0
  var green: Int                             = 0
  var ticks: Double                          = 0
  var lime: Int                              = 0
  var violet: Int                            = 0
  var black: Int                             = 0
  var brown: Int                             = 0
  var wind_constant_x: Int                   = 0
  var wind_constant_y: Int                   = 0
  var orange: Int                            = 0
  var blue: Int                              = 0
  var gravity_constant: Double               = 0
  var initial_range_x: Int                   = 0
  var max_pycor: Double                      = 0
  var min_pycor: Double                      = 0
  var viscosity_constant: Double             = 0
  def get_magenta(): Int                     = magenta
  def set_magenta(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("magenta") = DEFAULT_value
    magenta = DEFAULT_value
  }
  def get_initial_velocity_x(): Int = initial_velocity_x
  def set_initial_velocity_x(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("initial_velocity_x") = DEFAULT_value
    initial_velocity_x = DEFAULT_value
  }
  def get_pink(): Int = pink
  def set_pink(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("pink") = DEFAULT_value
    pink = DEFAULT_value
  }
  def get_initial_velocity_y(): Int = initial_velocity_y
  def set_initial_velocity_y(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("initial_velocity_y") = DEFAULT_value
    initial_velocity_y = DEFAULT_value
  }
  def get_yellow(): Int = yellow
  def set_yellow(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("yellow") = DEFAULT_value
    yellow = DEFAULT_value
  }
  def get_step_size(): Double = step_size
  def set_step_size(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("step_size") = DEFAULT_value
    step_size = DEFAULT_value
  }
  def get_turquoise(): Int = turquoise
  def set_turquoise(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("turquoise") = DEFAULT_value
    turquoise = DEFAULT_value
  }
  def get_max_number_of_particles(): Int = max_number_of_particles
  def set_max_number_of_particles(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("max_number_of_particles") = DEFAULT_value
    max_number_of_particles = DEFAULT_value
  }
  def get_cyan(): Int = cyan
  def set_cyan(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("cyan") = DEFAULT_value
    cyan = DEFAULT_value
  }
  def get_red(): Int = red
  def set_red(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("red") = DEFAULT_value
    red = DEFAULT_value
  }
  def get_gray(): Int = gray
  def set_gray(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("gray") = DEFAULT_value
    gray = DEFAULT_value
  }
  def get_white(): Double = white
  def set_white(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("white") = DEFAULT_value
    white = DEFAULT_value
  }
  def get_rate(): Double = rate
  def set_rate(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("rate") = DEFAULT_value
    rate = DEFAULT_value
  }
  def get_max_pxcor(): Double = max_pxcor
  def set_max_pxcor(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("max_pxcor") = DEFAULT_value
    max_pxcor = DEFAULT_value
  }
  def get_FUNCTION_RETURN_go(): Any = FUNCTION_RETURN_go
  def set_FUNCTION_RETURN_go(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_go") = DEFAULT_value
    FUNCTION_RETURN_go = DEFAULT_value
  }
  def get_min_pxcor(): Double = min_pxcor
  def set_min_pxcor(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("min_pxcor") = DEFAULT_value
    min_pxcor = DEFAULT_value
  }
  def get_FUNCTION_RETURN_setup(): Any = FUNCTION_RETURN_setup
  def set_FUNCTION_RETURN_setup(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_setup") = DEFAULT_value
    FUNCTION_RETURN_setup = DEFAULT_value
  }
  def get_sky(): Int = sky
  def set_sky(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("sky") = DEFAULT_value
    sky = DEFAULT_value
  }
  def get_green(): Int = green
  def set_green(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("green") = DEFAULT_value
    green = DEFAULT_value
  }
  def get_ticks(): Double = ticks
  def set_ticks(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("ticks") = DEFAULT_value
    ticks = DEFAULT_value
  }
  def get_lime(): Int = lime
  def set_lime(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("lime") = DEFAULT_value
    lime = DEFAULT_value
  }
  def get_violet(): Int = violet
  def set_violet(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("violet") = DEFAULT_value
    violet = DEFAULT_value
  }
  def get_black(): Int = black
  def set_black(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("black") = DEFAULT_value
    black = DEFAULT_value
  }
  def get_brown(): Int = brown
  def set_brown(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("brown") = DEFAULT_value
    brown = DEFAULT_value
  }
  def get_wind_constant_x(): Int = wind_constant_x
  def set_wind_constant_x(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("wind_constant_x") = DEFAULT_value
    wind_constant_x = DEFAULT_value
  }
  def get_wind_constant_y(): Int = wind_constant_y
  def set_wind_constant_y(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("wind_constant_y") = DEFAULT_value
    wind_constant_y = DEFAULT_value
  }
  def get_orange(): Int = orange
  def set_orange(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("orange") = DEFAULT_value
    orange = DEFAULT_value
  }
  def get_blue(): Int = blue
  def set_blue(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("blue") = DEFAULT_value
    blue = DEFAULT_value
  }
  def get_gravity_constant(): Double = gravity_constant
  def set_gravity_constant(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("gravity_constant") = DEFAULT_value
    gravity_constant = DEFAULT_value
  }
  def get_initial_range_x(): Int = initial_range_x
  def set_initial_range_x(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("initial_range_x") = DEFAULT_value
    initial_range_x = DEFAULT_value
  }
  def get_max_pycor(): Double = max_pycor
  def set_max_pycor(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("max_pycor") = DEFAULT_value
    max_pycor = DEFAULT_value
  }
  def get_min_pycor(): Double = min_pycor
  def set_min_pycor(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("min_pycor") = DEFAULT_value
    min_pycor = DEFAULT_value
  }
  def get_viscosity_constant(): Double = viscosity_constant
  def set_viscosity_constant(DEFAULT_value: Double): Unit = {
    DEFAULT_logs("viscosity_constant") = DEFAULT_value
    viscosity_constant = DEFAULT_value
  }
  override def main(): Unit = {
    markOverride("DEFAULT_UpdateFromParent", "DEFAULT_UpdateFromWorker")
    DEFAULT_observer = this
    max_pxcor = DEFAULT_BOARD_SIZE_X
    max_pycor = DEFAULT_BOARD_SIZE_Y
    min_pxcor = 0
    min_pycor = 0
    set_black(0)
    set_gray(5)
    set_white(9.9)
    set_red(15)
    set_orange(25)
    set_brown(35)
    set_yellow(45)
    set_green(55)
    set_lime(65)
    set_turquoise(75)
    set_cyan(85)
    set_sky(95)
    set_blue(105)
    set_violet(115)
    set_magenta(125)
    set_pink(135)
    val tmp_123 = {
      val tmp_126 = new WORKER_Observer()
      tmp_126.DEFAULT_observer = DEFAULT_observer
      tmp_126.DEFAULT_ASK = 59
      tmp_126.DEFAULT_Parent = this
      tmp_126.DEFAULT_myself = this
      tmp_126.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
      tmp_126
    }
    var tmp_124 = false
    while (!tmp_124) {
      val tmp_125 = asyncMessage(() => tmp_123.DEFAULT_is_worker_done())
      while (!tmp_125.isCompleted) {
        waitAndReply(1)
      }
      tmp_124 = tmp_125.popValue.get == true
    }
    tmp_123.DEFAULT_kill_worker()
    set_step_size(0.02)
    set_rate(0.1)
    set_max_number_of_particles(200)
    set_wind_constant_x(0)
    set_wind_constant_y(0)
    set_viscosity_constant(0.5)
    set_gravity_constant(8.9)
    set_initial_range_x(2)
    set_initial_velocity_x(7)
    set_initial_velocity_y(30)
    while (true) {
      val tmp_127 = {
        val tmp_130 = new WORKER_Observer()
        tmp_130.DEFAULT_observer = DEFAULT_observer
        tmp_130.DEFAULT_ASK = 52
        tmp_130.DEFAULT_Parent = this
        tmp_130.DEFAULT_myself = this
        tmp_130.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
        tmp_130
      }
      var tmp_128 = false
      while (!tmp_128) {
        val tmp_129 = asyncMessage(() => tmp_127.DEFAULT_is_worker_done())
        while (!tmp_129.isCompleted) {
          waitAndReply(1)
        }
        tmp_128 = tmp_129.popValue.get == true
      }
      tmp_127.DEFAULT_kill_worker()
      val tmp_131 = {
        val tmp_134 = new WORKER_Observer()
        tmp_134.DEFAULT_observer = DEFAULT_observer
        tmp_134.DEFAULT_ASK = 55
        tmp_134.DEFAULT_Parent = this
        tmp_134.DEFAULT_myself = this
        tmp_134.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
        tmp_134
      }
      var tmp_132 = false
      while (!tmp_132) {
        val tmp_133 = asyncMessage(() => tmp_131.DEFAULT_is_worker_done())
        while (!tmp_133.isCompleted) {
          waitAndReply(1)
        }
        tmp_132 = tmp_133.popValue.get == true
      }
      tmp_131.DEFAULT_kill_worker()
      val tmp_135 = {
        val tmp_138 = new WORKER_Observer()
        tmp_138.DEFAULT_observer = DEFAULT_observer
        tmp_138.DEFAULT_ASK = 51
        tmp_138.DEFAULT_Parent = this
        tmp_138.DEFAULT_myself = this
        tmp_138.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
        tmp_138
      }
      var tmp_136 = false
      while (!tmp_136) {
        val tmp_137 = asyncMessage(() => tmp_135.DEFAULT_is_worker_done())
        while (!tmp_137.isCompleted) {
          waitAndReply(1)
        }
        tmp_136 = tmp_137.popValue.get == true
      }
      tmp_135.DEFAULT_kill_worker()
      val tmp_139 = {
        val tmp_142 = new WORKER_Observer()
        tmp_142.DEFAULT_observer = DEFAULT_observer
        tmp_142.DEFAULT_ASK = 57
        tmp_142.DEFAULT_Parent = this
        tmp_142.DEFAULT_myself = this
        tmp_142.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
        tmp_142.FUNCTION_ARG_tick_advance_value = get_step_size()
        tmp_142
      }
      var tmp_140 = false
      while (!tmp_140) {
        val tmp_141 = asyncMessage(() => tmp_139.DEFAULT_is_worker_done())
        while (!tmp_141.isCompleted) {
          waitAndReply(1)
        }
        tmp_140 = tmp_141.popValue.get == true
      }
      tmp_139.DEFAULT_kill_worker()
      handleMessages()
      waitLabel(Turn, 1)
    }
  }
  override def DEFAULT_UpdateFromParent(dic: mutable.Map[String, Any]): Unit = {
    var values = dic.toList
    while (!values.isEmpty) {
      var kv = values.head
      values = values.tail
      if (kv._1 == "magenta") {
        magenta = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "initial_velocity_x") {
        initial_velocity_x = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "pink") {
        pink = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "initial_velocity_y") {
        initial_velocity_y = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "yellow") {
        yellow = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "step_size") {
        step_size = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "turquoise") {
        turquoise = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "max_number_of_particles") {
        max_number_of_particles = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "cyan") {
        cyan = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "red") {
        red = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "gray") {
        gray = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "white") {
        white = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "rate") {
        rate = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "max_pxcor") {
        max_pxcor = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_go") {
        FUNCTION_RETURN_go = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "min_pxcor") {
        min_pxcor = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "FUNCTION_RETURN_setup") {
        FUNCTION_RETURN_setup = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "sky") {
        sky = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "green") {
        green = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "ticks") {
        ticks = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "lime") {
        lime = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "violet") {
        violet = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "black") {
        black = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "brown") {
        brown = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "wind_constant_x") {
        wind_constant_x = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "wind_constant_y") {
        wind_constant_y = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "orange") {
        orange = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "blue") {
        blue = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "gravity_constant") {
        gravity_constant = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "initial_range_x") {
        initial_range_x = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "max_pycor") {
        max_pycor = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "min_pycor") {
        min_pycor = kv._2.asInstanceOf[Double]
      }
      if (kv._1 == "viscosity_constant") {
        viscosity_constant = kv._2.asInstanceOf[Double]
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
      if (kv._1 == "magenta") {
        magenta = kv._2.asInstanceOf[Int]
        DEFAULT_logs("magenta") = magenta
      }
      if (kv._1 == "initial_velocity_x") {
        initial_velocity_x = kv._2.asInstanceOf[Int]
        DEFAULT_logs("initial_velocity_x") = initial_velocity_x
      }
      if (kv._1 == "pink") {
        pink = kv._2.asInstanceOf[Int]
        DEFAULT_logs("pink") = pink
      }
      if (kv._1 == "initial_velocity_y") {
        initial_velocity_y = kv._2.asInstanceOf[Int]
        DEFAULT_logs("initial_velocity_y") = initial_velocity_y
      }
      if (kv._1 == "yellow") {
        yellow = kv._2.asInstanceOf[Int]
        DEFAULT_logs("yellow") = yellow
      }
      if (kv._1 == "step_size") {
        step_size = kv._2.asInstanceOf[Double]
        DEFAULT_logs("step_size") = step_size
      }
      if (kv._1 == "turquoise") {
        turquoise = kv._2.asInstanceOf[Int]
        DEFAULT_logs("turquoise") = turquoise
      }
      if (kv._1 == "max_number_of_particles") {
        max_number_of_particles = kv._2.asInstanceOf[Int]
        DEFAULT_logs("max_number_of_particles") = max_number_of_particles
      }
      if (kv._1 == "cyan") {
        cyan = kv._2.asInstanceOf[Int]
        DEFAULT_logs("cyan") = cyan
      }
      if (kv._1 == "red") {
        red = kv._2.asInstanceOf[Int]
        DEFAULT_logs("red") = red
      }
      if (kv._1 == "gray") {
        gray = kv._2.asInstanceOf[Int]
        DEFAULT_logs("gray") = gray
      }
      if (kv._1 == "white") {
        white = kv._2.asInstanceOf[Double]
        DEFAULT_logs("white") = white
      }
      if (kv._1 == "rate") {
        rate = kv._2.asInstanceOf[Double]
        DEFAULT_logs("rate") = rate
      }
      if (kv._1 == "max_pxcor") {
        max_pxcor = kv._2.asInstanceOf[Double]
        DEFAULT_logs("max_pxcor") = max_pxcor
      }
      if (kv._1 == "FUNCTION_RETURN_go") {
        FUNCTION_RETURN_go = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_go") = FUNCTION_RETURN_go
      }
      if (kv._1 == "min_pxcor") {
        min_pxcor = kv._2.asInstanceOf[Double]
        DEFAULT_logs("min_pxcor") = min_pxcor
      }
      if (kv._1 == "FUNCTION_RETURN_setup") {
        FUNCTION_RETURN_setup = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_setup") = FUNCTION_RETURN_setup
      }
      if (kv._1 == "sky") {
        sky = kv._2.asInstanceOf[Int]
        DEFAULT_logs("sky") = sky
      }
      if (kv._1 == "green") {
        green = kv._2.asInstanceOf[Int]
        DEFAULT_logs("green") = green
      }
      if (kv._1 == "ticks") {
        ticks = kv._2.asInstanceOf[Double]
        DEFAULT_logs("ticks") = ticks
      }
      if (kv._1 == "lime") {
        lime = kv._2.asInstanceOf[Int]
        DEFAULT_logs("lime") = lime
      }
      if (kv._1 == "violet") {
        violet = kv._2.asInstanceOf[Int]
        DEFAULT_logs("violet") = violet
      }
      if (kv._1 == "black") {
        black = kv._2.asInstanceOf[Int]
        DEFAULT_logs("black") = black
      }
      if (kv._1 == "brown") {
        brown = kv._2.asInstanceOf[Int]
        DEFAULT_logs("brown") = brown
      }
      if (kv._1 == "wind_constant_x") {
        wind_constant_x = kv._2.asInstanceOf[Int]
        DEFAULT_logs("wind_constant_x") = wind_constant_x
      }
      if (kv._1 == "wind_constant_y") {
        wind_constant_y = kv._2.asInstanceOf[Int]
        DEFAULT_logs("wind_constant_y") = wind_constant_y
      }
      if (kv._1 == "orange") {
        orange = kv._2.asInstanceOf[Int]
        DEFAULT_logs("orange") = orange
      }
      if (kv._1 == "blue") {
        blue = kv._2.asInstanceOf[Int]
        DEFAULT_logs("blue") = blue
      }
      if (kv._1 == "gravity_constant") {
        gravity_constant = kv._2.asInstanceOf[Double]
        DEFAULT_logs("gravity_constant") = gravity_constant
      }
      if (kv._1 == "initial_range_x") {
        initial_range_x = kv._2.asInstanceOf[Int]
        DEFAULT_logs("initial_range_x") = initial_range_x
      }
      if (kv._1 == "max_pycor") {
        max_pycor = kv._2.asInstanceOf[Double]
        DEFAULT_logs("max_pycor") = max_pycor
      }
      if (kv._1 == "min_pycor") {
        min_pycor = kv._2.asInstanceOf[Double]
        DEFAULT_logs("min_pycor") = min_pycor
      }
      if (kv._1 == "viscosity_constant") {
        viscosity_constant = kv._2.asInstanceOf[Double]
        DEFAULT_logs("viscosity_constant") = viscosity_constant
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
