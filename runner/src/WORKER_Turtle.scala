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
      if (DEFAULT_ASK == 4) {
        {
          set_forward_m((get_FUNCTION_ARG_forward_value() * Math.cos(get_angle())))
          set_xcord((get_xcord() + get_forward_m()))
          set_forward_m((get_FUNCTION_ARG_forward_value() * Math.sin(get_angle())))
          set_ycord((get_ycord() + get_forward_m()))
        }
        DEFAULT_ASK = -2
      }
      // setxy
      if (DEFAULT_ASK == 5) {
        {
          set_xcord(get_FUNCTION_ARG_setxy_x())
          set_ycord(get_FUNCTION_ARG_setxy_y())
        }
        DEFAULT_ASK = -2
      }
      // right
      if (DEFAULT_ASK == 6) {
        {
          set_angle((get_angle() - get_FUNCTION_ARG_right_value()))
        }
        DEFAULT_ASK = -2
      }
      // lambda_9
      if (DEFAULT_ASK == 10) {
        {
          set_color((Random.nextInt * 4))
          println("selected")
        }
        DEFAULT_ASK = -2
      }
      // home
      if (DEFAULT_ASK == 7) {
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
            tmp_3.DEFAULT_ASK = 4
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
      // fct
      if (DEFAULT_ASK == 3) {
        {
          set_FUNCTION_RETURN_fct((get_FUNCTION_ARG_fct_value() + 1))
        }
        DEFAULT_ASK = -2
      }
      // default_init
      if (DEFAULT_ASK == 8) {
        {
          set_angle(0.0)
        }
        DEFAULT_ASK = -2
      }
      // lambda_11
      if (DEFAULT_ASK == 12) {
        {
          println("filtered")
        }
        DEFAULT_ASK = -2
      }
      // lambda_13
      if (DEFAULT_ASK == 14) {
        {
          val tmp_5 = {
            val tmp_8 = new WORKER_Turtle()
            tmp_8.DEFAULT_observer = DEFAULT_observer
            tmp_8.DEFAULT_ASK = 1
            tmp_8.DEFAULT_Parent = this
            tmp_8.FUNCTION_ARG_left_value = 90.0
            tmp_8
          }
          var tmp_6 = false
          while (!tmp_6) {
            val tmp_7 = asyncMessage(() => tmp_5.DEFAULT_is_worker_done())
            while (!tmp_7.isCompleted) {
              waitAndReply(1)
            }
            tmp_6 = tmp_7.popValue.get == true
          }
          tmp_5.DEFAULT_kill_worker()
          val tmp_9 = {
            val tmp_12 = new WORKER_Turtle()
            tmp_12.DEFAULT_observer = DEFAULT_observer
            tmp_12.DEFAULT_ASK = 3
            tmp_12.DEFAULT_Parent = this
            tmp_12.FUNCTION_ARG_fct_value = DEFAULT_observer.get_test()
            tmp_12
          }
          var tmp_10 = false
          while (!tmp_10) {
            val tmp_11 = asyncMessage(() => tmp_9.DEFAULT_is_worker_done())
            while (!tmp_11.isCompleted) {
              waitAndReply(1)
            }
            tmp_10 = tmp_11.popValue.get == true
          }
          tmp_9.DEFAULT_kill_worker()
          println(FUNCTION_RETURN_fct)
        }
        DEFAULT_ASK = -2
      }
      // can-move
      if (DEFAULT_ASK == 0) {
        {
          set_angle(get_angle())
          set_FUNCTION_RETURN_can_move(true)
        }
        DEFAULT_ASK = -2
      }
      // lambda_15
      if (DEFAULT_ASK == 16) {
        {
          DEFAULT_observer.set_index((DEFAULT_observer.get_index() + 1))
          println(DEFAULT_observer.get_index())
        }
        DEFAULT_ASK = -2
      }
      // lambda_17
      if (DEFAULT_ASK == 18) {
        {
          set_speed(0)
          set_xcord(0)
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
