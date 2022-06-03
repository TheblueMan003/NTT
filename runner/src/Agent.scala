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
  def main(): Unit = {
    while (true) {
      handleMessages()
      waitLabel(Turn, 1)
    }
  }
  def DEFAULT_UpdateFromParent(dic: mutable.Map[String, Any]): Unit = {}
  def DEFAULT_UpdateFromWorker(dic: mutable.Map[String, Any]): Unit = {}
}
