package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class Patch(
    val DEFAULT_observer: Observer,
    val DEFAULT_X: Int,
    val DEFAULT_Y: Int,
    val DEFAULT_INITER: Int
) extends Agent(DEFAULT_observer, DEFAULT_X, DEFAULT_Y, DEFAULT_INITER) {
  val DEFAULT_LOG_Variables = mutable.Map[String, Any]()
  var DEFAULT_ASK           = -1
  var pycord: Any           = 0
  var pxcord: Any           = 0
  def get_pycord(): Any     = pycord
  def set_pycord(DEFAULT_value: Any): Unit = {
    DEFAULT_LOG_Variables("pycord") = DEFAULT_value
    pycord = DEFAULT_value
  }
  def get_pxcord(): Any = pxcord
  def set_pxcord(DEFAULT_value: Any): Unit = {
    DEFAULT_LOG_Variables("pxcord") = DEFAULT_value
    pxcord = DEFAULT_value
  }
  def main(): Unit = {
    while (true) {
      handleMessages()
      waitLabel(Turn, 1)
    }
  }
  def DEFAULT_UpdateFromParent(dic: mutable.Map[String, Any]): Unit = {
    dic.map(kv => {
      if (kv._1 == "pycord") {
        pycord = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "pxcord") {
        pxcord = kv._2.asInstanceOf[Any]
      }
      if (kv._1 == "default_is_done") {
        default_is_done = kv._2.asInstanceOf[Any]
      }
    })
  }
  def DEFAULT_UpdateFromParent(dic: mutable.Map[String, Any]): Unit = {
    dic.map(kv => {
      if (kv._1 == "pycord") {
        set_pycord(kv._2.asInstanceOf[Any])
      }
      if (kv._1 == "pxcord") {
        set_pxcord(kv._2.asInstanceOf[Any])
      }
      if (kv._1 == "default_is_done") {
        set_default_is_done(kv._2.asInstanceOf[Any])
      }
    })
  }
}
