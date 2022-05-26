package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class Observer(val DEFAULT_BOARD_X: Int, val DEFAULT_BOARD_Y: Int) {
  val wolves                = mutable.Set[Wolf]()
  val patches               = mutable.Set[Patch]()
  val observers             = mutable.Set[Observer]()
  val linkes                = mutable.Set[Link]()
  val turtles               = mutable.Set[Turtle]()
  val agents                = mutable.Set[Agent]()
  val DEFAULT_LOG_Variables = mutable.Map[String, Any]()
  var DEFAULT_ASK           = -1
  var test: Any             = 0
  var go_m: Any             = 0
  var go_p: Int             = 0
  var index: Any            = 0
  var go_c: Any             = 0
  def get_test(): Any       = test
  def set_test(DEFAULT_value: Any): Unit = {
    DEFAULT_LOG_Variables("test") = DEFAULT_value
    test = DEFAULT_value
  }
  def get_go_m(): Any = go_m
  def set_go_m(DEFAULT_value: Any): Unit = {
    DEFAULT_LOG_Variables("go_m") = DEFAULT_value
    go_m = DEFAULT_value
  }
  def get_go_p(): Int = go_p
  def set_go_p(DEFAULT_value: Int): Unit = {
    DEFAULT_LOG_Variables("go_p") = DEFAULT_value
    go_p = DEFAULT_value
  }
  def get_index(): Any = index
  def set_index(DEFAULT_value: Any): Unit = {
    DEFAULT_LOG_Variables("index") = DEFAULT_value
    index = DEFAULT_value
  }
  def get_go_c(): Any = go_c
  def set_go_c(DEFAULT_value: Any): Unit = {
    DEFAULT_LOG_Variables("go_c") = DEFAULT_value
    go_c = DEFAULT_value
  }
  def main(): Unit = {
    setup()
    while (true) {
      set_go_p(1)
      println("start of tick")
      val tmp_8 = DEFAULT_observer.get_turtles().toList.map(a => asyncMessage(() => a.get_go_p))
      while (!(tmp_8.nonEmpty && tmp_8.forall(x => x.isCompleted))) {
        waitAndReply(1)
      }
      val tmp_9: List[Int] = tmp_8.map(o => o.popValue.get).asInstanceOf[List[Int]]
      set_go_m(tmp_9)
      val tmp_10 =
        DEFAULT_observer.get_turtles().toList.map(s => asyncMessage(() => s.lambda_0(this)))
      while (!tmp_10.forall(_.isCompleted)) {
        waitAndReply(1)
      }
      println("middle of tick")
      val tmp_11 =
        DEFAULT_observer.get_turtles().toList.map(s => asyncMessage(() => s.lambda_2(this)))
      while (!tmp_11.forall(_.isCompleted)) {
        waitAndReply(1)
      }
      println("end of tick")
      val tmp_12 = DEFAULT_observer.get_turtles().toList.map(a => asyncMessage(() => a.get_speed))
      while (!(tmp_12.nonEmpty && tmp_12.forall(x => x.isCompleted))) {
        waitAndReply(1)
      }
      val tmp_13: List[Int] = tmp_12.map(o => o.popValue.get).asInstanceOf[List[Int]]
      set_go_c(tmp_13)
      handleMessages()
      waitLabel(Turn, 1)
    }
  }
  def DEFAULT_UpdateFromParent(dic: mutable.Map[String, Any]): Unit = {
    dic.map(kv => {
      if (kv._1 == "test") {
        test = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "go_m") {
        go_m = kv._2.asInstanceOf[Any]
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
      if (kv._1 == "default_is_done") {
        default_is_done = kv._2.asInstanceOf[Any]
      }
    })
  }
  def DEFAULT_UpdateFromParent(dic: mutable.Map[String, Any]): Unit = {
    dic.map(kv => {
      if (kv._1 == "test") {
        set_test(kv._2.asInstanceOf[Any])
      }
      if (kv._1 == "go_m") {
        set_go_m(kv._2.asInstanceOf[Any])
      }
      if (kv._1 == "go_p") {
        set_go_p(kv._2.asInstanceOf[Int])
      }
      if (kv._1 == "index") {
        set_index(kv._2.asInstanceOf[Any])
      }
      if (kv._1 == "go_c") {
        set_go_c(kv._2.asInstanceOf[Any])
      }
      if (kv._1 == "default_is_done") {
        set_default_is_done(kv._2.asInstanceOf[Any])
      }
    })
  }
  def setup(): Unit = {
    (1 to 10).map(_ => {
      turtles.add(new Turtle(this, 0, 0, 3))
    })
  }
}
