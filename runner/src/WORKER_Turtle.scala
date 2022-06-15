package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
import scala.util.Random
@lift
class WORKER_Turtle extends Turtle {
  var DEFAULT_Parent: Agent = null
  override def main(): Unit = {
    DEFAULT_UpdateFromParent(DEFAULT_Parent.get_DEFAULT_logs())
    while (true) {
      handleMessages()
      // rt
      if (DEFAULT_ASK == 8) {
        {
          set_heading(get_heading() - get_FUNCTION_ARG_rt_value())
        }
        DEFAULT_ASK = -2
      }
      // distance
      if (DEFAULT_ASK == 9) {
        {
          var tmp_5 = NetLogoUtils.toList[Turtle](get_FUNCTION_ARG_distance_value())
          var tmp_9 = List[Double]()
          while (tmp_5.nonEmpty) {
            val tmp_8 = {
              val tmp_11 = new WORKER_Turtle()
              tmp_11.DEFAULT_observer = DEFAULT_observer
              tmp_11.DEFAULT_ASK = 34
              tmp_11.DEFAULT_Parent = tmp_5.head
              tmp_11.DEFAULT_myself = this
              tmp_11.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
              tmp_11
            }
            var tmp_7 = false
            while (!tmp_7) {
              val tmp_6 = asyncMessage(() => tmp_8.DEFAULT_is_worker_done())
              while (!tmp_6.isCompleted) {
                waitAndReply(1)
              }
              tmp_7 = tmp_6.popValue.get
            }
            val tmp_10 = asyncMessage(() => tmp_8.get_FUNCTION_RETURN_lambda_33())
            while (!tmp_10.isCompleted) {
              waitAndReply(1)
            }
            tmp_9 = tmp_10.popValue.get :: tmp_9
            asyncMessage(() => tmp_8.DEFAULT_kill_worker())
            tmp_5 = tmp_5.tail
          }
          var tmp_12 = NetLogoUtils.toList[Turtle](get_FUNCTION_ARG_distance_value())
          var tmp_16 = List[Double]()
          while (tmp_12.nonEmpty) {
            val tmp_15 = {
              val tmp_18 = new WORKER_Turtle()
              tmp_18.DEFAULT_observer = DEFAULT_observer
              tmp_18.DEFAULT_ASK = 36
              tmp_18.DEFAULT_Parent = tmp_12.head
              tmp_18.DEFAULT_myself = this
              tmp_18.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
              tmp_18
            }
            var tmp_14 = false
            while (!tmp_14) {
              val tmp_13 = asyncMessage(() => tmp_15.DEFAULT_is_worker_done())
              while (!tmp_13.isCompleted) {
                waitAndReply(1)
              }
              tmp_14 = tmp_13.popValue.get
            }
            val tmp_17 = asyncMessage(() => tmp_15.get_FUNCTION_RETURN_lambda_35())
            while (!tmp_17.isCompleted) {
              waitAndReply(1)
            }
            tmp_16 = tmp_17.popValue.get :: tmp_16
            asyncMessage(() => tmp_15.DEFAULT_kill_worker())
            tmp_12 = tmp_12.tail
          }
          val tmp_19 = {
            val tmp_22 = new WORKER_Turtle()
            tmp_22.DEFAULT_observer = DEFAULT_observer
            tmp_22.DEFAULT_ASK = 10
            tmp_22.DEFAULT_Parent = this
            tmp_22.DEFAULT_myself = this
            tmp_22.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_22.FUNCTION_ARG_distancexy_x = tmp_9.head
            tmp_22.FUNCTION_ARG_distancexy_y = tmp_16.head
            tmp_22
          }
          var tmp_20 = false
          while (!tmp_20) {
            val tmp_21 = asyncMessage(() => tmp_19.DEFAULT_is_worker_done())
            while (!tmp_21.isCompleted) {
              waitAndReply(1)
            }
            tmp_20 = tmp_21.popValue.get == true
          }
          tmp_19.DEFAULT_kill_worker()
          set_FUNCTION_RETURN_distance(FUNCTION_RETURN_distancexy)
        }
        DEFAULT_ASK = -2
      }
      // distancexy
      if (DEFAULT_ASK == 10) {
        {
          set_distancexy_dx(Math.pow(get_xcor() - get_FUNCTION_ARG_distancexy_x(), 2))
          set_distancexy_dy(Math.pow(get_ycor() - get_FUNCTION_ARG_distancexy_y(), 2))
          val tmp_24 = {
            val tmp_27 = new WORKER_Turtle()
            tmp_27.DEFAULT_observer = DEFAULT_observer
            tmp_27.DEFAULT_ASK = 4
            tmp_27.DEFAULT_Parent = this
            tmp_27.DEFAULT_myself = this
            tmp_27.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_27
          }
          var tmp_25 = false
          while (!tmp_25) {
            val tmp_26 = asyncMessage(() => tmp_24.DEFAULT_is_worker_done())
            while (!tmp_26.isCompleted) {
              waitAndReply(1)
            }
            tmp_25 = tmp_26.popValue.get == true
          }
          tmp_24.DEFAULT_kill_worker()
          val tmp_28 = {
            val tmp_31 = new WORKER_Turtle()
            tmp_31.DEFAULT_observer = DEFAULT_observer
            tmp_31.DEFAULT_ASK = 5
            tmp_31.DEFAULT_Parent = this
            tmp_31.DEFAULT_myself = this
            tmp_31.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_31
          }
          var tmp_29 = false
          while (!tmp_29) {
            val tmp_30 = asyncMessage(() => tmp_28.DEFAULT_is_worker_done())
            while (!tmp_30.isCompleted) {
              waitAndReply(1)
            }
            tmp_29 = tmp_30.popValue.get == true
          }
          tmp_28.DEFAULT_kill_worker()
          set_distancexy_d(Math.round(FUNCTION_RETURN_dx + FUNCTION_RETURN_dy))
          set_FUNCTION_RETURN_distancexy(get_distancexy_d())
        }
        DEFAULT_ASK = -2
      }
      // lt
      if (DEFAULT_ASK == 0) {
        {
          val tmp_33 = {
            val tmp_36 = new WORKER_Turtle()
            tmp_36.DEFAULT_observer = DEFAULT_observer
            tmp_36.DEFAULT_ASK = 6
            tmp_36.DEFAULT_Parent = this
            tmp_36.DEFAULT_myself = this
            tmp_36.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_36.FUNCTION_ARG_left_value = get_FUNCTION_ARG_lt_value()
            tmp_36
          }
          var tmp_34 = false
          while (!tmp_34) {
            val tmp_35 = asyncMessage(() => tmp_33.DEFAULT_is_worker_done())
            while (!tmp_35.isCompleted) {
              waitAndReply(1)
            }
            tmp_34 = tmp_35.popValue.get == true
          }
          tmp_33.DEFAULT_kill_worker()
        }
        DEFAULT_ASK = -2
      }
      // hide-turtle
      if (DEFAULT_ASK == 1) {
        {
          set_visible(false)
        }
        DEFAULT_ASK = -2
      }
      // facexy
      if (DEFAULT_ASK == 2) {
        {
          set_heading(
            Math.atan(
              get_FUNCTION_ARG_facexy_y() - get_ycor(),
              get_FUNCTION_ARG_facexy_x() - get_xcor()
            )
          )
        }
        DEFAULT_ASK = -2
      }
      // apply-viscosity
      if (DEFAULT_ASK == 11) {
        {
          set_force_accumulator_x(
            get_force_accumulator_x() - DEFAULT_observer.get_viscosity_constant() * get_velocity_x()
          )
          set_force_accumulator_y(
            get_force_accumulator_y() - DEFAULT_observer.get_viscosity_constant() * get_velocity_y()
          )
        }
        DEFAULT_ASK = -2
      }
      // apply-wind
      if (DEFAULT_ASK == 3) {
        {
          set_force_accumulator_x(
            get_force_accumulator_x() + DEFAULT_observer.get_wind_constant_x()
          )
          set_force_accumulator_y(
            get_force_accumulator_y() + DEFAULT_observer.get_wind_constant_y()
          )
        }
        DEFAULT_ASK = -2
      }
      // dx
      if (DEFAULT_ASK == 4) {
        {
          set_FUNCTION_RETURN_dx(Math.cos(get_heading()))
        }
        DEFAULT_ASK = -2
      }
      // dy
      if (DEFAULT_ASK == 5) {
        {
          set_FUNCTION_RETURN_dy(Math.sin(get_heading()))
        }
        DEFAULT_ASK = -2
      }
      // lambda_23
      if (DEFAULT_ASK == 24) {
        {
          set_force_accumulator_x(0)
          set_force_accumulator_y(0)
          val tmp_39 = {
            val tmp_42 = new WORKER_Turtle()
            tmp_42.DEFAULT_observer = DEFAULT_observer
            tmp_42.DEFAULT_ASK = 12
            tmp_42.DEFAULT_Parent = this
            tmp_42.DEFAULT_myself = this
            tmp_42.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_42
          }
          var tmp_40 = false
          while (!tmp_40) {
            val tmp_41 = asyncMessage(() => tmp_39.DEFAULT_is_worker_done())
            while (!tmp_41.isCompleted) {
              waitAndReply(1)
            }
            tmp_40 = tmp_41.popValue.get == true
          }
          tmp_39.DEFAULT_kill_worker()
          val tmp_43 = {
            val tmp_46 = new WORKER_Turtle()
            tmp_46.DEFAULT_observer = DEFAULT_observer
            tmp_46.DEFAULT_ASK = 3
            tmp_46.DEFAULT_Parent = this
            tmp_46.DEFAULT_myself = this
            tmp_46.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_46
          }
          var tmp_44 = false
          while (!tmp_44) {
            val tmp_45 = asyncMessage(() => tmp_43.DEFAULT_is_worker_done())
            while (!tmp_45.isCompleted) {
              waitAndReply(1)
            }
            tmp_44 = tmp_45.popValue.get == true
          }
          tmp_43.DEFAULT_kill_worker()
          val tmp_47 = {
            val tmp_50 = new WORKER_Turtle()
            tmp_50.DEFAULT_observer = DEFAULT_observer
            tmp_50.DEFAULT_ASK = 11
            tmp_50.DEFAULT_Parent = this
            tmp_50.DEFAULT_myself = this
            tmp_50.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_50
          }
          var tmp_48 = false
          while (!tmp_48) {
            val tmp_49 = asyncMessage(() => tmp_47.DEFAULT_is_worker_done())
            while (!tmp_49.isCompleted) {
              waitAndReply(1)
            }
            tmp_48 = tmp_49.popValue.get == true
          }
          tmp_47.DEFAULT_kill_worker()
        }
        DEFAULT_ASK = -2
      }
      // lambda_25
      if (DEFAULT_ASK == 26) {
        {
          set_color(DEFAULT_observer.get_blue())
          set_size(0.1 + Random.nextFloat)
          set_mass(Math.pow(get_size(), 2))
          set_velocity_x(
            DEFAULT_observer.get_initial_velocity_x() - Random.nextFloat + Random.nextFloat
          )
          set_velocity_y(DEFAULT_observer.get_initial_velocity_y())
        }
        DEFAULT_ASK = -2
      }
      // lambda_27
      if (DEFAULT_ASK == 28) {
        {
          set_velocity_x(
            get_velocity_x() + get_force_accumulator_x() * DEFAULT_observer.get_step_size()
          )
          set_velocity_y(
            get_velocity_y() + get_force_accumulator_y() * DEFAULT_observer.get_step_size()
          )
          set_lambda_27_step_x(get_velocity_x() * DEFAULT_observer.get_step_size())
          set_lambda_27_step_y(get_velocity_y() * DEFAULT_observer.get_step_size())
          var tmp_51 = DEFAULT_observer.get_patches()
          var tmp_53 = List[(Patch, Int, Int)]()
          while (!tmp_51.isEmpty) {
            val tmp_52 = tmp_51.head
            tmp_51 = tmp_51.tail
            tmp_53 = tmp_53 ::: List((tmp_52, tmp_52.get_pxcor(), tmp_52.get_pycor()))
          }
          val tmp_54 = DEFAULT_ASK_STACK(0).asInstanceOf[Turtle].get_lambda_27_step_x()
          val tmp_55 = DEFAULT_ASK_STACK(0).asInstanceOf[Turtle].get_lambda_27_step_y()
          if (NetLogoUtils.filterPosition(tmp_53, tmp_54, tmp_55).head == null) {
            deleted = true
          }
          set_lambda_27_new_x(
            get_xcor() + DEFAULT_ASK_STACK(0).asInstanceOf[Turtle].get_lambda_27_step_x()
          )
          set_lambda_27_new_y(
            get_ycor() + DEFAULT_ASK_STACK(0).asInstanceOf[Turtle].get_lambda_27_step_y()
          )
          val tmp_56 = {
            val tmp_59 = new WORKER_Turtle()
            tmp_59.DEFAULT_observer = DEFAULT_observer
            tmp_59.DEFAULT_ASK = 2
            tmp_59.DEFAULT_Parent = this
            tmp_59.DEFAULT_myself = this
            tmp_59.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_59.FUNCTION_ARG_facexy_x =
              DEFAULT_ASK_STACK(0).asInstanceOf[Turtle].get_lambda_27_new_x()
            tmp_59.FUNCTION_ARG_facexy_y =
              DEFAULT_ASK_STACK(0).asInstanceOf[Turtle].get_lambda_27_new_y()
            tmp_59
          }
          var tmp_57 = false
          while (!tmp_57) {
            val tmp_58 = asyncMessage(() => tmp_56.DEFAULT_is_worker_done())
            while (!tmp_58.isCompleted) {
              waitAndReply(1)
            }
            tmp_57 = tmp_58.popValue.get == true
          }
          tmp_56.DEFAULT_kill_worker()
          val tmp_60 = {
            val tmp_63 = new WORKER_Turtle()
            tmp_63.DEFAULT_observer = DEFAULT_observer
            tmp_63.DEFAULT_ASK = 15
            tmp_63.DEFAULT_Parent = this
            tmp_63.DEFAULT_myself = this
            tmp_63.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_63.FUNCTION_ARG_setxy_x =
              DEFAULT_ASK_STACK(0).asInstanceOf[Turtle].get_lambda_27_new_x()
            tmp_63.FUNCTION_ARG_setxy_y =
              DEFAULT_ASK_STACK(0).asInstanceOf[Turtle].get_lambda_27_new_y()
            tmp_63
          }
          var tmp_61 = false
          while (!tmp_61) {
            val tmp_62 = asyncMessage(() => tmp_60.DEFAULT_is_worker_done())
            while (!tmp_62.isCompleted) {
              waitAndReply(1)
            }
            tmp_61 = tmp_62.popValue.get == true
          }
          tmp_60.DEFAULT_kill_worker()
        }
        DEFAULT_ASK = -2
      }
      // lambda_29
      if (DEFAULT_ASK == 30) {
        set_FUNCTION_RETURN_lambda_29(get_xcor())
        DEFAULT_ASK = -2
      }
      // apply-gravity
      if (DEFAULT_ASK == 12) {
        {
          set_force_accumulator_y(
            get_force_accumulator_y() - DEFAULT_observer.get_gravity_constant() / get_mass()
          )
        }
        DEFAULT_ASK = -2
      }
      // can-move?
      if (DEFAULT_ASK == 13) {
        {
          set_heading(get_heading())
          set_FUNCTION_RETURN_can_move_(true)
        }
        DEFAULT_ASK = -2
      }
      // forward
      if (DEFAULT_ASK == 14) {
        {
          set_forward_m(get_FUNCTION_ARG_forward_value() * Math.cos(get_heading()))
          set_xcor(get_xcor() + get_forward_m())
          set_forward_m(get_FUNCTION_ARG_forward_value() * Math.sin(get_heading()))
          set_ycor(get_ycor() + get_forward_m())
        }
        DEFAULT_ASK = -2
      }
      // setxy
      if (DEFAULT_ASK == 15) {
        {
          set_xcor(get_FUNCTION_ARG_setxy_x())
          set_ycor(get_FUNCTION_ARG_setxy_y())
          set_pxcor(get_FUNCTION_ARG_setxy_x().toInt)
          set_pycor(get_FUNCTION_ARG_setxy_y().toInt)
        }
        DEFAULT_ASK = -2
      }
      // move-to
      if (DEFAULT_ASK == 16) {
        {
          var tmp_66 = NetLogoUtils.toList[Turtle](get_FUNCTION_ARG_move_to_a())
          var tmp_70 = List[Double]()
          while (tmp_66.nonEmpty) {
            val tmp_69 = {
              val tmp_72 = new WORKER_Turtle()
              tmp_72.DEFAULT_observer = DEFAULT_observer
              tmp_72.DEFAULT_ASK = 38
              tmp_72.DEFAULT_Parent = tmp_66.head
              tmp_72.DEFAULT_myself = this
              tmp_72.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
              tmp_72
            }
            var tmp_68 = false
            while (!tmp_68) {
              val tmp_67 = asyncMessage(() => tmp_69.DEFAULT_is_worker_done())
              while (!tmp_67.isCompleted) {
                waitAndReply(1)
              }
              tmp_68 = tmp_67.popValue.get
            }
            val tmp_71 = asyncMessage(() => tmp_69.get_FUNCTION_RETURN_lambda_37())
            while (!tmp_71.isCompleted) {
              waitAndReply(1)
            }
            tmp_70 = tmp_71.popValue.get :: tmp_70
            asyncMessage(() => tmp_69.DEFAULT_kill_worker())
            tmp_66 = tmp_66.tail
          }
          var tmp_73 = NetLogoUtils.toList[Turtle](get_FUNCTION_ARG_move_to_a())
          var tmp_77 = List[Double]()
          while (tmp_73.nonEmpty) {
            val tmp_76 = {
              val tmp_79 = new WORKER_Turtle()
              tmp_79.DEFAULT_observer = DEFAULT_observer
              tmp_79.DEFAULT_ASK = 40
              tmp_79.DEFAULT_Parent = tmp_73.head
              tmp_79.DEFAULT_myself = this
              tmp_79.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
              tmp_79
            }
            var tmp_75 = false
            while (!tmp_75) {
              val tmp_74 = asyncMessage(() => tmp_76.DEFAULT_is_worker_done())
              while (!tmp_74.isCompleted) {
                waitAndReply(1)
              }
              tmp_75 = tmp_74.popValue.get
            }
            val tmp_78 = asyncMessage(() => tmp_76.get_FUNCTION_RETURN_lambda_39())
            while (!tmp_78.isCompleted) {
              waitAndReply(1)
            }
            tmp_77 = tmp_78.popValue.get :: tmp_77
            asyncMessage(() => tmp_76.DEFAULT_kill_worker())
            tmp_73 = tmp_73.tail
          }
          val tmp_80 = {
            val tmp_83 = new WORKER_Turtle()
            tmp_83.DEFAULT_observer = DEFAULT_observer
            tmp_83.DEFAULT_ASK = 15
            tmp_83.DEFAULT_Parent = this
            tmp_83.DEFAULT_myself = this
            tmp_83.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_83.FUNCTION_ARG_setxy_x = tmp_70.head
            tmp_83.FUNCTION_ARG_setxy_y = tmp_77.head
            tmp_83
          }
          var tmp_81 = false
          while (!tmp_81) {
            val tmp_82 = asyncMessage(() => tmp_80.DEFAULT_is_worker_done())
            while (!tmp_82.isCompleted) {
              waitAndReply(1)
            }
            tmp_81 = tmp_82.popValue.get == true
          }
          tmp_80.DEFAULT_kill_worker()
        }
        DEFAULT_ASK = -2
      }
      // right
      if (DEFAULT_ASK == 17) {
        {
          set_heading(get_heading() - get_FUNCTION_ARG_right_value())
        }
        DEFAULT_ASK = -2
      }
      // home
      if (DEFAULT_ASK == 18) {
        {
          val tmp_84 = {
            val tmp_87 = new WORKER_Turtle()
            tmp_87.DEFAULT_observer = DEFAULT_observer
            tmp_87.DEFAULT_ASK = 15
            tmp_87.DEFAULT_Parent = this
            tmp_87.DEFAULT_myself = this
            tmp_87.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_87.FUNCTION_ARG_setxy_x = 0.0
            tmp_87.FUNCTION_ARG_setxy_y = 0.0
            tmp_87
          }
          var tmp_85 = false
          while (!tmp_85) {
            val tmp_86 = asyncMessage(() => tmp_84.DEFAULT_is_worker_done())
            while (!tmp_86.isCompleted) {
              waitAndReply(1)
            }
            tmp_85 = tmp_86.popValue.get == true
          }
          tmp_84.DEFAULT_kill_worker()
        }
        DEFAULT_ASK = -2
      }
      // face
      if (DEFAULT_ASK == 19) {
        {
          var tmp_88 = NetLogoUtils.toList[Turtle](get_FUNCTION_ARG_face_value())
          var tmp_92 = List[Double]()
          while (tmp_88.nonEmpty) {
            val tmp_91 = {
              val tmp_94 = new WORKER_Turtle()
              tmp_94.DEFAULT_observer = DEFAULT_observer
              tmp_94.DEFAULT_ASK = 30
              tmp_94.DEFAULT_Parent = tmp_88.head
              tmp_94.DEFAULT_myself = this
              tmp_94.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
              tmp_94
            }
            var tmp_90 = false
            while (!tmp_90) {
              val tmp_89 = asyncMessage(() => tmp_91.DEFAULT_is_worker_done())
              while (!tmp_89.isCompleted) {
                waitAndReply(1)
              }
              tmp_90 = tmp_89.popValue.get
            }
            val tmp_93 = asyncMessage(() => tmp_91.get_FUNCTION_RETURN_lambda_29())
            while (!tmp_93.isCompleted) {
              waitAndReply(1)
            }
            tmp_92 = tmp_93.popValue.get :: tmp_92
            asyncMessage(() => tmp_91.DEFAULT_kill_worker())
            tmp_88 = tmp_88.tail
          }
          set_face_x(tmp_92.head)
          var tmp_95 = NetLogoUtils.toList[Turtle](get_FUNCTION_ARG_face_value())
          var tmp_99 = List[Double]()
          while (tmp_95.nonEmpty) {
            val tmp_98 = {
              val tmp_101 = new WORKER_Turtle()
              tmp_101.DEFAULT_observer = DEFAULT_observer
              tmp_101.DEFAULT_ASK = 32
              tmp_101.DEFAULT_Parent = tmp_95.head
              tmp_101.DEFAULT_myself = this
              tmp_101.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
              tmp_101
            }
            var tmp_97 = false
            while (!tmp_97) {
              val tmp_96 = asyncMessage(() => tmp_98.DEFAULT_is_worker_done())
              while (!tmp_96.isCompleted) {
                waitAndReply(1)
              }
              tmp_97 = tmp_96.popValue.get
            }
            val tmp_100 = asyncMessage(() => tmp_98.get_FUNCTION_RETURN_lambda_31())
            while (!tmp_100.isCompleted) {
              waitAndReply(1)
            }
            tmp_99 = tmp_100.popValue.get :: tmp_99
            asyncMessage(() => tmp_98.DEFAULT_kill_worker())
            tmp_95 = tmp_95.tail
          }
          set_face_y(tmp_99.head)
          val tmp_102 = {
            val tmp_105 = new WORKER_Turtle()
            tmp_105.DEFAULT_observer = DEFAULT_observer
            tmp_105.DEFAULT_ASK = 2
            tmp_105.DEFAULT_Parent = this
            tmp_105.DEFAULT_myself = this
            tmp_105.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_105.FUNCTION_ARG_facexy_x = get_face_x()
            tmp_105.FUNCTION_ARG_facexy_y = get_face_y()
            tmp_105
          }
          var tmp_103 = false
          while (!tmp_103) {
            val tmp_104 = asyncMessage(() => tmp_102.DEFAULT_is_worker_done())
            while (!tmp_104.isCompleted) {
              waitAndReply(1)
            }
            tmp_103 = tmp_104.popValue.get == true
          }
          tmp_102.DEFAULT_kill_worker()
        }
        DEFAULT_ASK = -2
      }
      // left
      if (DEFAULT_ASK == 6) {
        {
          set_heading(get_heading() + get_FUNCTION_ARG_left_value())
        }
        DEFAULT_ASK = -2
      }
      // show-turtle
      if (DEFAULT_ASK == 20) {
        {
          set_visible(true)
        }
        DEFAULT_ASK = -2
      }
      // default_init
      if (DEFAULT_ASK == 21) {
        {
          set_heading(0.0)
          val tmp_106 = {
            val tmp_109 = new WORKER_Turtle()
            tmp_109.DEFAULT_observer = DEFAULT_observer
            tmp_109.DEFAULT_ASK = 9
            tmp_109.DEFAULT_Parent = this
            tmp_109.DEFAULT_myself = this
            tmp_109.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_109.FUNCTION_ARG_distance_value = DEFAULT_myself
            tmp_109
          }
          var tmp_107 = false
          while (!tmp_107) {
            val tmp_108 = asyncMessage(() => tmp_106.DEFAULT_is_worker_done())
            while (!tmp_108.isCompleted) {
              waitAndReply(1)
            }
            tmp_107 = tmp_108.popValue.get == true
          }
          tmp_106.DEFAULT_kill_worker()
          FUNCTION_RETURN_distance
          val tmp_110 = {
            val tmp_113 = new WORKER_Turtle()
            tmp_113.DEFAULT_observer = DEFAULT_observer
            tmp_113.DEFAULT_ASK = 19
            tmp_113.DEFAULT_Parent = this
            tmp_113.DEFAULT_myself = this
            tmp_113.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_113.FUNCTION_ARG_face_value = DEFAULT_myself
            tmp_113
          }
          var tmp_111 = false
          while (!tmp_111) {
            val tmp_112 = asyncMessage(() => tmp_110.DEFAULT_is_worker_done())
            while (!tmp_112.isCompleted) {
              waitAndReply(1)
            }
            tmp_111 = tmp_112.popValue.get == true
          }
          tmp_110.DEFAULT_kill_worker()
        }
        DEFAULT_ASK = -2
      }
      // lambda_31
      if (DEFAULT_ASK == 32) {
        set_FUNCTION_RETURN_lambda_31(get_ycor())
        DEFAULT_ASK = -2
      }
      // lambda_33
      if (DEFAULT_ASK == 34) {
        set_FUNCTION_RETURN_lambda_33(get_xcor())
        DEFAULT_ASK = -2
      }
      // lambda_35
      if (DEFAULT_ASK == 36) {
        set_FUNCTION_RETURN_lambda_35(get_ycor())
        DEFAULT_ASK = -2
      }
      // lambda_37
      if (DEFAULT_ASK == 38) {
        set_FUNCTION_RETURN_lambda_37(get_xcor())
        DEFAULT_ASK = -2
      }
      // fd
      if (DEFAULT_ASK == 7) {
        {
          val tmp_118 = {
            val tmp_121 = new WORKER_Turtle()
            tmp_121.DEFAULT_observer = DEFAULT_observer
            tmp_121.DEFAULT_ASK = 14
            tmp_121.DEFAULT_Parent = this
            tmp_121.DEFAULT_myself = this
            tmp_121.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
            tmp_121.FUNCTION_ARG_forward_value = get_FUNCTION_ARG_fd_value()
            tmp_121
          }
          var tmp_119 = false
          while (!tmp_119) {
            val tmp_120 = asyncMessage(() => tmp_118.DEFAULT_is_worker_done())
            while (!tmp_120.isCompleted) {
              waitAndReply(1)
            }
            tmp_119 = tmp_120.popValue.get == true
          }
          tmp_118.DEFAULT_kill_worker()
        }
        DEFAULT_ASK = -2
      }
      // lambda_39
      if (DEFAULT_ASK == 40) {
        set_FUNCTION_RETURN_lambda_39(get_ycor())
        DEFAULT_ASK = -2
      }
      // default_init_turtle
      if (DEFAULT_ASK == 22) {
        {
          set_shape_size(1)
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
