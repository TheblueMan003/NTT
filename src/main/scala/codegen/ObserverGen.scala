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

object ObserverGen{
    /**
     * Generate Observer sets.
     *
     * @return	List[Instruction]
     */
    def generateObserverSets()(implicit context: Context): List[Instruction] = {
        context.getBreeds().map(b =>
            InstructionGen(f"val ${Renamer.toValidName(b.pluralName)} = mutable.Set[${b.className}]()")
        ).toList
    }
}