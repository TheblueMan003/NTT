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