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

object ToolGen{
    /**
      * Generate a switch for the functions according to their lambdaIndex
      *
      * @param breed: breed of the functions
      * @param indexValue: variable for which the switch is generated
      * @param functions: functions to generate the switch for
      * @param flag: flag for the functions
      * @param ending: instruction to do after the switch
      * @param context : context of the code generation
      * @return
      */
    def generateSwitchInstruction(breed: Breed, indexValue: String, functions: Iterable[LinkedFunction], flag: Flag, ending: Instruction = EmptyInstruction)(implicit context: Context):Instruction = {
        if (functions.isEmpty){
            EmptyInstruction
        }
        else {
            val lst = functions.map(f => 
                InstructionList(
                    InstructionGen(f"//${f.name}"),
                    InstructionCompose(f"if ($indexValue == ${f.lambdaIndex})", 
                        InstructionBlock(
                            ContentGen.generate(f.symTree)(f, breed, context, flag),
                            ending
                        )
                    )
                )
            )
            InstructionList(lst.toList)
        }
    }
}