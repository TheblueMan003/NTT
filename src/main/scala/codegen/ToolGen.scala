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
    def generateSwitchInstruction(breed: Breed, indexValue: String, functions: Iterable[LinkedFunction], flag: Flag)(implicit context: Context):Instruction = {
        if (functions.isEmpty){
            EmptyInstruction
        }
        else {
            val lst = functions.map(f => 
                InstructionCompose(f"if ($indexValue == ${f.lambdaIndex})", 
                    ContentGen.generate(f.symTree)(f, breed, context, flag)
                )
            )
            InstructionList(lst.toList)
        }
    }
}