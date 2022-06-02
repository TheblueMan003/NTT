package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class WORKER_Turtle extends Turtle {
  var DEFAULT_Parent: Turtle = null
  override def main(): Unit = {
    DEFAULT_UpdateFromParent(DEFAULT_Parent.get_DEFAULT_logs())
    while (true) {
      handleMessages()
      // forward
      if (DEFAULT_ASK == 3) {
        {
          set_forward_m((get_FUNCTION_ARG_forward_value() * Math.cos(get_angle())))
          set_xcord((get_xcord() + get_forward_m()))
          set_forward_m((get_FUNCTION_ARG_forward_value() * Math.sin(get_angle())))
          set_ycord((get_ycord() + get_forward_m()))
        }
        DEFAULT_ASK = -2
      }
      // setxy
      if (DEFAULT_ASK == 4) {
        {
          set_xcord(get_FUNCTION_ARG_setxy_x())
          set_ycord(get_FUNCTION_ARG_setxy_y())
        }
        DEFAULT_ASK = -2
      }
      // right
      if (DEFAULT_ASK == 5) {
        {
          set_angle((get_angle() - get_FUNCTION_ARG_right_value()))
        }
        DEFAULT_ASK = -2
      }
      // lambda_8
      if (DEFAULT_ASK == 9) {
        {
          set_color((Random.nextInt * 4))
          println("selected")
        }
        DEFAULT_ASK = -2
      }
      // home
      if (DEFAULT_ASK == 6) {
        {
          set_xcord(0.0)
          set_ycord(0.0)
        }
        DEFAULT_ASK = -2
      }
      // fw
      if (DEFAULT_ASK == 2) {
        {
          val tmp_0 = {
            val tmp_3 = new WORKER_Turtle()
            tmp_3.DEFAULT_observer = DEFAULT_observer
            tmp_3.DEFAULT_ASK = 3
            tmp_3.DEFAULT_Parent = this
            tmp_3.FUNCTION_ARG_forward_value = get_FUNCTION_ARG_fw_value()
            tmp_3
          }
          var tmp_1 = false
          while (!tmp_1) {
            val tmp_2 = asyncMessage(() => tmp_0.DEFAULT_is_worker_done())
            while (!tmp_2.isCompleted) {
              waitAndReply(1)
            }
            tmp_1 = tmp_2.popValue.get == true
          }
          tmp_0.DEFAULT_kill_worker()
        }
        DEFAULT_ASK = -2
      }
      // left
      if (DEFAULT_ASK == 1) {
        {
          set_angle((get_angle() + get_FUNCTION_ARG_left_value()))
        }
        DEFAULT_ASK = -2
      }
      // lambda_10
      if (DEFAULT_ASK == 11) {
        {
          println("filtered")
        }
        DEFAULT_ASK = -2
      }
      // default_init
      if (DEFAULT_ASK == 7) {
        {
          set_angle(0.0)
        }
        DEFAULT_ASK = -2
      }
      // lambda_12
      if (DEFAULT_ASK == 13) {
        {
          val tmp_4 = {
            val tmp_7 = new WORKER_Turtle()
            tmp_7.DEFAULT_observer = DEFAULT_observer
            tmp_7.DEFAULT_ASK = 1
            tmp_7.DEFAULT_Parent = this
            tmp_7.FUNCTION_ARG_left_value = 90.0
            tmp_7
          }
          var tmp_5 = false
          while (!tmp_5) {
            val tmp_6 = asyncMessage(() => tmp_4.DEFAULT_is_worker_done())
            while (!tmp_6.isCompleted) {
              waitAndReply(1)
            }
            tmp_5 = tmp_6.popValue.get == true
          }
          tmp_4.DEFAULT_kill_worker()
          println("turn")
        }
        DEFAULT_ASK = -2
      }
      // lambda_14
      if (DEFAULT_ASK == 15) {
        {
          set_speed(0)
          set_xcord(0)
        }
        DEFAULT_ASK = -2
      }
      // can-move
      if (DEFAULT_ASK == 0) {
        {
          set_angle(get_angle())
          val tmp_8 = true
          tmp_8
        }
        DEFAULT_ASK = -2
      }
      asyncMessage(() => DEFAULT_Parent.DEFAULT_UpdateFromWorker(DEFAULT_logs))
      while (DEFAULT_ASK == -2) waitAndReply(1)
      waitLabel(Turn, 1)
    }
  }
  def DEFAULT_is_worker_done(): Boolean = DEFAULT_ASK == -2
  def DEFAULT_kill_worker(): Unit = {
    deleted = true
  }
}
