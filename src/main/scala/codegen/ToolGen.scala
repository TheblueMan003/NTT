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

object ToolGen{
    def generateSwitchInstruction(breed: Breed, indexValue: String, functions: Iterable[LinkedFunction])(implicit context: Context):Instruction = {
        if (functions.isEmpty){
            EmptyInstruction
        }
        else {
            InstructionCompose(f"$indexValue match", InstructionBlock(
                functions.map(f => InstructionCompose(f"case ${f.lambdaIndex} => ",  ContentGen.generate(f.symTree)(f, breed))).toList
            ))
        }
    }
}