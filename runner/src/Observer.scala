package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class Observer extends Agent {
  val wolves                                 = mutable.Set[Wolf]()
  def get_wolves(): mutable.Set[Wolf]        = wolves
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
  var test: Int                              = 0
  var FUNCTION_RETURN_go: Any                = 0
  var go_p: Int                              = 0
  var index: Int                             = 0
  var go_c: Any                              = 0
  var FUNCTION_RETURN_setup: Any             = 0
  def get_test(): Int                        = test
  def set_test(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("test") = DEFAULT_value
    test = DEFAULT_value
  }
  def get_FUNCTION_RETURN_go(): Any = FUNCTION_RETURN_go
  def set_FUNCTION_RETURN_go(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_go") = DEFAULT_value
    FUNCTION_RETURN_go = DEFAULT_value
  }
  def get_go_p(): Int = go_p
  def set_go_p(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("go_p") = DEFAULT_value
    go_p = DEFAULT_value
  }
  def get_index(): Int = index
  def set_index(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("index") = DEFAULT_value
    index = DEFAULT_value
  }
  def get_go_c(): Any = go_c
  def set_go_c(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("go_c") = DEFAULT_value
    go_c = DEFAULT_value
  }
  def get_FUNCTION_RETURN_setup(): Any = FUNCTION_RETURN_setup
  def set_FUNCTION_RETURN_setup(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_RETURN_setup") = DEFAULT_value
    FUNCTION_RETURN_setup = DEFAULT_value
  }
  override def main(): Unit = {
    markOverride("DEFAULT_UpdateFromParent", "DEFAULT_UpdateFromWorker")
    DEFAULT_observer = this
    setup()
    while (true) {
      set_go_p(1)
      println("start of tick")
      val tmp_14 = turtles.toList.map(s => {
        val tmp_17 = new WORKER_Turtle()
        tmp_17.DEFAULT_observer = DEFAULT_observer
        tmp_17.DEFAULT_ASK = 10
        tmp_17.DEFAULT_Parent = s
        tmp_17
      })
      var tmp_16 = false
      while (!tmp_16) {
        val tmp_15 = tmp_14.map(s => asyncMessage(() => s.DEFAULT_is_worker_done()))
        while (!tmp_15.forall(_.isCompleted)) {
          waitAndReply(1)
        }
        tmp_16 = tmp_15.map(o => o.popValue.get).toList.forall(_ == true)
      }
      tmp_14.map(s => asyncMessage(() => s.DEFAULT_kill_worker()))
      val tmp_18 = turtles.toList.map(s => asyncMessage(() => s.DEFAULT_PREDICATE_0()))
      while (!tmp_18.forall(_.isCompleted)) {
        waitAndReply(1)
      }
      val tmp_19 = tmp_18.map(o => o.popValue.get).filter(_ != null)
      val tmp_20 = tmp_19.toList.map(s => {
        val tmp_23 = new WORKER_Turtle()
        tmp_23.DEFAULT_observer = DEFAULT_observer
        tmp_23.DEFAULT_ASK = 12
        tmp_23.DEFAULT_Parent = s
        tmp_23
      })
      var tmp_22 = false
      while (!tmp_22) {
        val tmp_21 = tmp_20.map(s => asyncMessage(() => s.DEFAULT_is_worker_done()))
        while (!tmp_21.forall(_.isCompleted)) {
          waitAndReply(1)
        }
        tmp_22 = tmp_21.map(o => o.popValue.get).toList.forall(_ == true)
      }
      tmp_20.map(s => asyncMessage(() => s.DEFAULT_kill_worker()))
      println("middle of tick")
      val tmp_24 = turtles.toList.map(s => {
        val tmp_27 = new WORKER_Turtle()
        tmp_27.DEFAULT_observer = DEFAULT_observer
        tmp_27.DEFAULT_ASK = 14
        tmp_27.DEFAULT_Parent = s
        tmp_27
      })
      var tmp_26 = false
      while (!tmp_26) {
        val tmp_25 = tmp_24.map(s => asyncMessage(() => s.DEFAULT_is_worker_done()))
        while (!tmp_25.forall(_.isCompleted)) {
          waitAndReply(1)
        }
        tmp_26 = tmp_25.map(o => o.popValue.get).toList.forall(_ == true)
      }
      tmp_24.map(s => asyncMessage(() => s.DEFAULT_kill_worker()))
      println("part 2")
      val tmp_28 = turtles.toList.map(s => {
        val tmp_31 = new WORKER_Turtle()
        tmp_31.DEFAULT_observer = DEFAULT_observer
        tmp_31.DEFAULT_ASK = 16
        tmp_31.DEFAULT_Parent = s
        tmp_31
      })
      var tmp_30 = false
      while (!tmp_30) {
        val tmp_29 = tmp_28.map(s => asyncMessage(() => s.DEFAULT_is_worker_done()))
        while (!tmp_29.forall(_.isCompleted)) {
          waitAndReply(1)
        }
        tmp_30 = tmp_29.map(o => o.popValue.get).toList.forall(_ == true)
      }
      tmp_28.map(s => asyncMessage(() => s.DEFAULT_kill_worker()))
      println("end of tick")
      val tmp_32 = turtles.toList.map(a => asyncMessage(() => a.get_speed))
      while (!(tmp_32.nonEmpty && tmp_32.forall(x => x.isCompleted))) {
        waitAndReply(1)
      }
      val tmp_33: List[Int] = tmp_32.map(o => o.popValue.get).asInstanceOf[List[Int]]
      set_go_c(tmp_33)
      handleMessages()
      waitLabel(Turn, 1)
    }
  }
  override def DEFAULT_UpdateFromParent(dic: mutable.Map[String, Any]): Unit = {
    var values = dic.toList
    while (!values.isEmpty) {
      var kv = values.head
      values = values.tail
      if (kv._1 == "test") {
        test = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "FUNCTION_RETURN_go") {
        FUNCTION_RETURN_go = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "go_p") {
        go_p = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "index") {
        index = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "go_c") {
        go_c = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "FUNCTION_RETURN_setup") {
        FUNCTION_RETURN_setup = kv._2.asInstanceOf[Any]
      }
    }
  }
  override def DEFAULT_UpdateFromWorker(dic: mutable.Map[String, Any]): Unit = {
    var values = dic.toList
    while (!values.isEmpty) {
      var kv = values.head
      values = values.tail
      if (kv._1 == "test") {
        test = kv._2.asInstanceOf[Int]
        DEFAULT_logs("test") = test
      }
      if (kv._1 == "FUNCTION_RETURN_go") {
        FUNCTION_RETURN_go = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_go") = FUNCTION_RETURN_go
      }
      if (kv._1 == "go_p") {
        go_p = kv._2.asInstanceOf[Int]
        DEFAULT_logs("go_p") = go_p
      }
      if (kv._1 == "index") {
        index = kv._2.asInstanceOf[Int]
        DEFAULT_logs("index") = index
      }
      if (kv._1 == "go_c") {
        go_c = kv._2.asInstanceOf[Any]
        DEFAULT_logs("go_c") = go_c
      }
      if (kv._1 == "FUNCTION_RETURN_setup") {
        FUNCTION_RETURN_setup = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_RETURN_setup") = FUNCTION_RETURN_setup
      }
    }
  }
  def setup(): Unit = {
    var tmp_35 = 0
    while (tmp_35 < 10) {
      {
        val tmp_34 = new Turtle()
        tmp_34.DEFAULT_observer = this
        tmp_34.DEFAULT_INITER = 18
        turtles.add(tmp_34)
      }
      tmp_35 = tmp_35 + 1
    }
  }
}
