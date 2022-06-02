package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class Agent extends Actor {
  val DEFAULT_logs                                 = mutable.Map[String, Any]()
  def get_DEFAULT_logs(): mutable.Map[String, Any] = DEFAULT_logs
  var DEFAULT_ASK                                  = -1
  var DEFAULT_INITER                               = -1
  var DEFAULT_observer: Observer                   = null
  var FUNCTION_ARG_fct_value: Any                  = 0
  def get_FUNCTION_ARG_fct_value(): Any            = FUNCTION_ARG_fct_value
  def set_FUNCTION_ARG_fct_value(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("FUNCTION_ARG_fct_value") = DEFAULT_value
    FUNCTION_ARG_fct_value = DEFAULT_value
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
      if (kv._1 == "FUNCTION_ARG_fct_value") {
        FUNCTION_ARG_fct_value = kv._2.asInstanceOf[Any]
      }
    }
  }
  def DEFAULT_UpdateFromWorker(dic: mutable.Map[String, Any]): Unit = {
    var values = dic.toList
    while (!values.isEmpty) {
      var kv = values.head
      values = values.tail
      if (kv._1 == "FUNCTION_ARG_fct_value") {
        FUNCTION_ARG_fct_value = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_ARG_fct_value") = FUNCTION_ARG_fct_value
      }
    }
  }
}
