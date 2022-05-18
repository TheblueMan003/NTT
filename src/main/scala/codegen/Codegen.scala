package codegen

import analyser.SymTree._
import analyser.SymTree
import utils.Context
import ast.Breed
import ast.{LinkedFunction, Function, BaseFunction}
import ast.Variable
import analyser.Types._
import analyser.Type
import utils.Reporter

object CodeGen{
    def generate(context: Context): List[ClassFile] = {
        generateAllClass()(context)
    }
    private def generateAllClass()(implicit context: Context) = {
        MainGen.generateMainClass() ::
            List(MainGen.generateMainInit()):::
            context.getBreeds().map(BreedGen.generateBreed(_)).toList
    }
}