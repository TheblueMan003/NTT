package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class Patch extends Agent {
  var pycord: Any       = 0
  var pxcord: Any       = 0
  def get_pycord(): Any = pycord
  def set_pycord(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("pycord") = DEFAULT_value
    pycord = DEFAULT_value
  }
  def get_pxcord(): Any = pxcord
  def set_pxcord(DEFAULT_value: Any): Unit = {
    DEFAULT_logs("pxcord") = DEFAULT_value
    pxcord = DEFAULT_value
  }
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
      if (kv._1 == "pycord") {
        pycord = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "pxcord") {
        pxcord = kv._2.asInstanceOf[Any]
      }
    }
  }
  override def DEFAULT_UpdateFromWorker(dic: mutable.Map[String, Any]): Unit = {
    var values = dic.toList
    while (!values.isEmpty) {
      var kv = values.head
      values = values.tail
      if (kv._1 == "pycord") {
        pycord = kv._2.asInstanceOf[Any]
        DEFAULT_logs("pycord") = pycord
      }
      if (kv._1 == "pxcord") {
        pxcord = kv._2.asInstanceOf[Any]
        DEFAULT_logs("pxcord") = pxcord
      }
    }
  }
}
