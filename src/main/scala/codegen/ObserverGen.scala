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
            List(
                InstructionGen(f"val ${Renamer.toValidName(b.pluralName)} = mutable.Set[${b.className}]()"),
                generateObserverSetGetter(b)
            )
        ).toList.flatten
    }

    /**
     * Generate Observer getters.
     *
     * @return	Instruction
     */
    def generateObserverSetGetter(b: Breed)(implicit context: Context): Instruction={
        val typ = b.className
        val name = Renamer.toValidName(b.pluralName)
        InstructionGen(f"def get_${name}(): mutable.Set[${typ}] = ${Renamer.toValidName(b.pluralName)}")
    }
}