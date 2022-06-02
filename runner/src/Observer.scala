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
  var test: Any                              = 0
  var go_p: Int                              = 0
  var index: Any                             = 0
  var go_c: Any                              = 0
  def get_test(): Any                        = test
  def set_test(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("test") = DEFAULT_value
    test = DEFAULT_value
  }
  def get_go_p(): Int = go_p
  def set_go_p(DEFAULT_value: Int): Unit = {
    DEFAULT_logs("go_p") = DEFAULT_value
    go_p = DEFAULT_value
  }
  def get_index(): Any = index
  def set_index(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("index") = DEFAULT_value
    index = DEFAULT_value
  }
  def get_go_c(): Any = go_c
  def set_go_c(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("go_c") = DEFAULT_value
    go_c = DEFAULT_value
  }
  override def main(): Unit = {
    markOverride("DEFAULT_UpdateFromParent", "DEFAULT_UpdateFromWorker")
    DEFAULT_observer = this
    setup()
    while (true) {
      set_go_p(1)
      println("start of tick")
      val tmp_9 = turtles.toList.map(s => {
        val tmp_12 = new WORKER_Turtle()
        tmp_12.DEFAULT_observer = DEFAULT_observer
        tmp_12.DEFAULT_ASK = 9
        tmp_12.DEFAULT_Parent = s
        tmp_12
      })
      var tmp_11 = false
      while (!tmp_11) {
        val tmp_10 = tmp_9.map(s => asyncMessage(() => s.DEFAULT_is_worker_done()))
        while (!tmp_10.forall(_.isCompleted)) {
          waitAndReply(1)
        }
        tmp_11 = tmp_10.map(o => o.popValue.get).toList.forall(_ == true)
      }
      tmp_9.map(s => asyncMessage(() => s.DEFAULT_kill_worker()))
      val tmp_13 = turtles.toList.map(s => asyncMessage(() => s.DEFAULT_PREDICATE_0()))
      while (!tmp_13.forall(_.isCompleted)) {
        waitAndReply(1)
      }
      val tmp_14 = tmp_13.map(o => o.popValue.get).filter(_ != null)
      val tmp_15 = tmp_14.toList.map(s => {
        val tmp_18 = new WORKER_Turtle()
        tmp_18.DEFAULT_observer = DEFAULT_observer
        tmp_18.DEFAULT_ASK = 11
        tmp_18.DEFAULT_Parent = s
        tmp_18
      })
      var tmp_17 = false
      while (!tmp_17) {
        val tmp_16 = tmp_15.map(s => asyncMessage(() => s.DEFAULT_is_worker_done()))
        while (!tmp_16.forall(_.isCompleted)) {
          waitAndReply(1)
        }
        tmp_17 = tmp_16.map(o => o.popValue.get).toList.forall(_ == true)
      }
      tmp_15.map(s => asyncMessage(() => s.DEFAULT_kill_worker()))
      println("middle of tick")
      val tmp_19 = turtles.toList.map(s => {
        val tmp_22 = new WORKER_Turtle()
        tmp_22.DEFAULT_observer = DEFAULT_observer
        tmp_22.DEFAULT_ASK = 13
        tmp_22.DEFAULT_Parent = s
        tmp_22
      })
      var tmp_21 = false
      while (!tmp_21) {
        val tmp_20 = tmp_19.map(s => asyncMessage(() => s.DEFAULT_is_worker_done()))
        while (!tmp_20.forall(_.isCompleted)) {
          waitAndReply(1)
        }
        tmp_21 = tmp_20.map(o => o.popValue.get).toList.forall(_ == true)
      }
      tmp_19.map(s => asyncMessage(() => s.DEFAULT_kill_worker()))
      println("end of tick")
      val tmp_23 = turtles.toList.map(a => asyncMessage(() => a.get_speed))
      while (!(tmp_23.nonEmpty && tmp_23.forall(x => x.isCompleted))) {
        waitAndReply(1)
      }
      val tmp_24: List[Int] = tmp_23.map(o => o.popValue.get).asInstanceOf[List[Int]]
      set_go_c(tmp_24)
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
        test = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "go_p") {
        go_p = kv._2.asInstanceOf[Int]
      }
      if (kv._1 == "index") {
        index = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "go_c") {
        go_c = kv._2.asInstanceOf[Any]
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
      if (kv._1 == "test") {
        test = kv._2.asInstanceOf[Any]
        DEFAULT_logs("test") = test
      }
      if (kv._1 == "go_p") {
        go_p = kv._2.asInstanceOf[Int]
        DEFAULT_logs("go_p") = go_p
      }
      if (kv._1 == "index") {
        index = kv._2.asInstanceOf[Any]
        DEFAULT_logs("index") = index
      }
      if (kv._1 == "go_c") {
        go_c = kv._2.asInstanceOf[Any]
        DEFAULT_logs("go_c") = go_c
      }
      if (kv._1 == "FUNCTION_ARG_fct_value") {
        FUNCTION_ARG_fct_value = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_ARG_fct_value") = FUNCTION_ARG_fct_value
      }
    }
  }
  def setup(): Unit = {
    var tmp_26 = 0
    while (tmp_26 < 10) {
      {
        val tmp_25 = new Turtle()
        tmp_25.DEFAULT_observer = this
        tmp_25.DEFAULT_INITER = 15
        turtles.add(tmp_25)
      }
      tmp_26 = tmp_26 + 1
    }
  }
}
