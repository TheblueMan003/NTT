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
  override def DEFAULT_UpdateFromParent(dic: mutable.Map[String, Any]): Unit = {}
  override def DEFAULT_UpdateFromWorker(dic: mutable.Map[String, Any]): Unit = {}
}
