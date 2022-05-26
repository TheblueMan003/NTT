package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor

object MainInit extends {
  val liftedMain = meta.classLifting.liteLift {
    def apply(size_x: Int, size_y: Int): List[Actor] = {
      val observer = new Observer(size_x, size_y)
      val patches = (1 to size_x)
        .map(x =>
          (1 to size_y).map(y => {
            new Patch(observer, x, y, -1)
          })
        )
        .flatten
      observer :: patches
    }
  }
}
