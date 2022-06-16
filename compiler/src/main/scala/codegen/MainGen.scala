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
            List(),
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
                InstructionGen(f"val $observerVariableName = new ${observer}"),
                InstructionGen(f"$observerVariableName.${ObserverGen.boardSizeX} = size_x"),
                InstructionGen(f"$observerVariableName.${ObserverGen.boardSizeY} = size_y"),
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
        val lst = if (breeds.size > 0) {breeds.map(getClassesWithObject(_)).flatten.reduce(_ + ", "+_)} else {""}
        InstructionGen(f"compileSims(List($lst), Some(mainClass))")
    }


    /**
      * Get the list of classes with objects associated to the breed
      *
      * @param breed: Breed for which we want to get the class
      * @param context: Context of compilation
      * @return
      */
    def getClassesWithObject(breed: Breed)(implicit context: Context): List[String] = {
        if (breed == context.getAgentBreed()){
            List(breed.singularName)
        }
        else{
            List(breed.singularName, f"WORKER_${breed.singularName}")
        }
    }

    /**
     * Generate Reflect for a breed.
     *
     * @param	breed	
     * @return	Instruction
     */
    def generateClassWithObject(breed: Breed)(implicit context: Context):Instruction = {
        if (breed == context.getAgentBreed()){
            InstructionGen(f"val ${breed.singularName} : ClassWithObject[${breed.className}] = ${breed.className}.reflect(IR)")
        }
        else{
            InstructionList(
                InstructionGen(f"val ${breed.singularName} : ClassWithObject[${breed.className}] = ${breed.className}.reflect(IR)"),
                InstructionGen(f"val WORKER_${breed.singularName} : ClassWithObject[WORKER_${breed.className}] = WORKER_${breed.className}.reflect(IR)")
            )
        }
    }

    /**
     * Generate the code to generate the patch grid.
     *
     * @return	Instruction
     */
    def generateGrid()(implicit context: Context):Instruction = {
        val xcordName = "\"pxcor\""
        val ycordName = "\"pycor\""
        InstructionCompose(f"val ${MainGen.patchVariableName} = (1 to size_x).map(x => (1 to size_y).map(y => {",
        InstructionBlock(
                InstructionGen(f"val patch = new Patch()"),
                InstructionGen(f"patch.pxcor = x"),
                InstructionGen(f"patch.pycor = y"),
                InstructionGen(f"patch.${BreedGen.logName}($xcordName) = x"),
                InstructionGen(f"patch.${BreedGen.logName}($ycordName) = y"),
                InstructionGen(f"patch.${BreedGen.observerVariable} = ${MainGen.observerVariableName}"),
                InstructionGen(f"${MainGen.observerVariableName}.patches.add(patch)"),
                InstructionGen(f"patch"),
        ), "}).toList).toList.flatten")
    }
}