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

object MainGen{
    val observerVariableName = "observer"
    val patchVariableName = "patches"

    /**
     * Generate MainInit Class.
     *
     * @return	ClassFile
     */
    def generateMainInit()(implicit context: Context): ClassFile = {
        ClassFile(
            CodeGen.imports,
            "",
            "object",
            "MainInit",
            "",
            List(""),
            generateMainInitContent(), 
            List(),
        )
    }

    /**
     * Generate MainInit Class Content.
     *
     * @return	Instruction
     */
    def generateMainInitContent()(implicit context: Context):List[Instruction] = {
        val observer = context.getObserverBreed().className
        List(
        InstructionCompose("val liftedMain = meta.classLifting.liteLift",
        InstructionBlock(
            InstructionCompose("def apply(size_x: Int, size_y: Int): List[Actor] = ", InstructionBlock(
                InstructionGen(f"val $observerVariableName = new ${observer}(size_x, size_y)"),
                generateGrid(),
                InstructionGen(f"$observerVariableName :: $patchVariableName")
            ))
        )))
    }

    /**
     * Generate Main Class.
     *
     * @return	ClassFile
     */
    def generateMainClass()(implicit context: Context): ClassFile = {
        ClassFile(
            CodeGen.imports,
            "",
            "object",
            "Simulation",
            "",
            List("App"),
            generateMainClassContent(), 
            List(),
        )
    }

    /**
     * Generate Main Class Content.
     *
     * @return	List[Instruction]
     */
    def generateMainClassContent()(implicit context: Context):List[Instruction] = {
        InstructionGen("val mainClass = MainInit.liftedMain")::
            context.getBreeds().map(generateClassWithObject(_)).toList :::
            List(generateMainClassStart())
    }

    /**
     * Generate MainInit Class Content.
     *
     * @return	Instruction
     */
    def generateMainClassStart()(implicit context: Context):Instruction = {
        val breeds = context.getBreeds()
        val lst = if (breeds.size > 0) {breeds.map(b => b.singularName).reduce(_ + ", "+_)} else {""}
        InstructionGen(f"compileSims(List($lst), Some(mainClass))")
    }

    /**
     * Generate Reflect for a breed.
     *
     * @param	breed	
     * @return	Instruction
     */
    def generateClassWithObject(breed: Breed):Instruction = {
        InstructionGen(f"val ${breed.singularName} : ClassWithObject[${breed.className}] = ${breed.className}.reflect(IR)")
    }

    /**
     * Generate the code to generate the patch grid.
     *
     * @return	Instruction
     */
    def generateGrid()(implicit context: Context):Instruction = {
        InstructionCompose(f"val ${MainGen.patchVariableName} = (1 to size_x).map(x => (1 to size_y).map(y =>",
        InstructionBlock(
                InstructionGen(f"new Patch(${MainGen.observerVariableName}, x, y, -1)")
        ), ")).flatten")
    }
}