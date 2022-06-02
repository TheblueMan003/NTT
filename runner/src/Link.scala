package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class Link extends Agent {
  override def main(): Unit = {
    markOverride("DEFAULT_UpdateFromParent", "DEFAULT_UpdateFromWorker")
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
      if (kv._1 == "FUNCTION_ARG_fct_value") {
        FUNCTION_ARG_fct_value = kv._2.asInstanceOf[Any]
        DEFAULT_logs("FUNCTION_ARG_fct_value") = FUNCTION_ARG_fct_value
      }
    }
  }
}
