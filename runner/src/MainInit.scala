package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor
import scala.util.Random

object MainInit {
  val liftedMain = meta.classLifting.liteLift {
    def apply(size_x: Int, size_y: Int): List[Actor] = {
      val observer = new Observer
      observer.DEFAULT_BOARD_SIZE_X = size_x
      observer.DEFAULT_BOARD_SIZE_Y = size_y
      val patches = (1 to size_x)
        .map(x =>
          (1 to size_y)
            .map(y => {
              {
                val patch = new Patch()
                patch.pxcor = x
                patch.pycor = y
                patch.DEFAULT_observer = observer
                patch
              }
            })
            .toList
        )
        .toList
        .flatten
      observer :: patches
    }
  }
}
