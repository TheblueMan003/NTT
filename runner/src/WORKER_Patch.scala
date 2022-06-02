package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
@lift
class WORKER_Patch extends Patch {
  var DEFAULT_Parent: Patch = null
  override def main(): Unit = {
    DEFAULT_UpdateFromParent(DEFAULT_Parent.get_DEFAULT_logs())
    while (true) {
      handleMessages()
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
