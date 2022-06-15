package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
import scala.util.Random
@lift
class WORKER_Patch extends Patch {
  var DEFAULT_Parent: Agent = null
  override def main(): Unit = {
    DEFAULT_UpdateFromParent(DEFAULT_Parent.get_DEFAULT_logs())
    while (true) {
      handleMessages()
      // lambda_0
      if (DEFAULT_ASK == 1) {
        {
          var tmp_0 = NetLogoUtils.toList[Observer](DEFAULT_observer.get_observers())
          while (tmp_0.nonEmpty) {
            val tmp_3 = {
              val tmp_4 = new WORKER_Observer()
              tmp_4.DEFAULT_observer = DEFAULT_observer
              tmp_4.DEFAULT_ASK = 3
              tmp_4.DEFAULT_Parent = tmp_0.head
              tmp_4.DEFAULT_myself = this
              tmp_4.DEFAULT_ASK_STACK = DEFAULT_ASK_STACK
              tmp_4
            }
            var tmp_2 = false
            while (!tmp_2) {
              val tmp_1 = asyncMessage(() => tmp_3.DEFAULT_is_worker_done())
              while (!tmp_1.isCompleted) {
                waitAndReply(1)
              }
              tmp_2 = tmp_1.popValue.get
            }
            asyncMessage(() => tmp_3.DEFAULT_kill_worker())
            tmp_0 = tmp_0.tail
          }
        }
        DEFAULT_ASK = -2
      }
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
