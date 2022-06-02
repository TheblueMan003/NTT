package codegen

import analyser.SymTree._
import analyser.SymTree
import utils.Context
import netlogo.Breed
import netlogo.{LinkedFunction, Function, BaseFunction}
import netlogo.Variable
import netlogo.Types._
import netlogo.Type
import utils.Reporter

object CodeGen{
    val imports = List(
        "package example",
        "package netlogo",

        "import meta.classLifting.SpecialInstructions._",
        "import squid.quasi.lift",
        "import scala.collection.mutable",
        "import meta.runtime.Actor"
    )

    def generate(context: Context): List[ClassFile] = {
        generateAllClass()(context)
    }
    private def generateAllClass()(implicit context: Context) = {
        MainGen.generateMainClass() ::
            List(MainGen.generateMainInit()):::
            context.getBreeds().map(BreedGen.generateBreed(_)).toList.flatten
    }
}

trait Flag
object Flag{
    case object ObserverMainFunctionFlag extends Flag
    case object MainFunctionFlag extends Flag
    case object WorkerFunctionFlag extends Flag
    case object FunctionFlag extends Flag
}