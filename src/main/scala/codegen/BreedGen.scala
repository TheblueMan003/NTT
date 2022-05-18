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

object BreedGen{
    val initerVariableName = "DEFAULT_INITER"
    val askVaraibleName = "DEFAULT_ASK"

    /**
     * Generate Breed Class.
     *
     * @param	breed	
     * @return	ClassFile
     */
    def generateBreed(breed: Breed)(implicit context: Context): ClassFile = {
        ClassFile(
            List(),
            "@lift",
            "class",
            breed.className,
            getClassArguments(breed),
            getParents
            (breed),
            generateBreedFields(breed), 
            generateBreedFunctions(breed)
        )
    }

    /**
     * Generate Field for breed Class.
     *
     * @param	breed	
     * @return	List[Instruction] 
     */
    def generateBreedFields(breed: Breed)(implicit context: Context):List[Instruction] = {
        if (breed == context.getObserverBreed()){
            ObserverGen.generateObserverSets():::ContentGen.generateVariables(breed.getAllVariables())
        } else {
            ContentGen.generateVariables(breed.getAllVariables())
        }
    }

    /**
     * Generate Functions for a Breed.
     *
     * @param	breed	
     * @return	List[FunctionGen]
     */
    def generateBreedFunctions(breed: Breed)(implicit context: Context): List[FunctionGen] = {
        generateMainFunction(breed)::
            List(generateUpdater(breed)):::
        breed.getAllFunctions().filter(_.name != "go").map{
            _ match {
                case lc: LinkedFunction => ContentGen.generate(lc, breed)
                case _ => null
            }
        }.filter(_ != null).toList
    }

    /**
     * @param	breed	
     * @return	FunctionGenerator for the main function of the breed
     */
    def generateMainFunction(breed: Breed)(implicit context: Context): FunctionGen = {
        val init = if (breed == context.getObserverBreed()){
            InstructionGen("setup()")
        }
        else{
            generateSettupFunctionSwitch(breed)
        }
        val go = if (breed == context.getObserverBreed()){
            val fct = breed.getFunction("go").asInstanceOf[LinkedFunction]
            InstructionList(ContentGen.generate(fct.symTree)(fct, breed).asInstanceOf[InstructionBlock].content)
        }
        else{
            InstructionGen("")
        }

        FunctionGen(getMainFunctionName(breed), List(), "Unit", 
            InstructionBlock(List(
                init,
                InstructionCompose(f"while(true)", 
                InstructionBlock(List(
                    go,
                    InstructionGen("handleMessages()"),
                    generateMainFunctionSwitch(breed),
                    InstructionGen("waitLabel(Turn, 1)")
                ))
                )  
            ))
        )
    }

    def generateMainFunctionSwitch(breed: Breed)(implicit context: Context): Instruction = {
        if (breed == context.getObserverBreed() || breed.getAllAskedFunctions().isEmpty){
            EmptyInstruction
        }
        else{
            ToolGen.generateSwitchInstruction(breed, askVaraibleName, breed.getAllAskedFunctions().map(_.asInstanceOf[LinkedFunction]))
        }
    }

    def generateSettupFunctionSwitch(breed: Breed)(implicit context: Context): Instruction = {
        if (breed == context.getObserverBreed() || breed.getAllCreateFunctions().isEmpty){
            EmptyInstruction
        }
        else{
            ToolGen.generateSwitchInstruction(breed, initerVariableName, breed.getAllCreateFunctions().map(_.asInstanceOf[LinkedFunction]))
        }
    }

    /**
     * @param	breed
     * @return	List of parents for the breed class
     */
    def getParents(breed: Breed)(implicit context: Context): List[String] = {
        if (breed.parent == null){
            List()
        }
        else if (breed.parent == context.getBreedSingular("agent")){
            List("Actor")
        }
        else{
            List(breed.parent.className)
        }
    }

    def generateUpdater(breed: Breed)(implicit context: Context): FunctionGen = {
        val p = "\""
        FunctionGen("DEFAULT_Update", List(new Variable("dic")), "Unit",
        InstructionBlock(List(
            InstructionCompose("dic.map((k,v) => k match",
            InstructionBlock(
                breed.getAllVariables().map(x => InstructionGen(f"case $p${x.name}$p => ${x.name} = v.asInstanceOf[${Type.toString(x.getType())}]")).toList
            ),")"
        ))))
    }

    /**
     * @param	breed
     * @return	Name of the main function for the breed class
     */
    def getMainFunctionName(breed: Breed)(implicit context: Context): String = {
        if (breed.parent == null){
            "main"
        }
        else if (breed.parent == context.getBreedSingular("agent")){
            "main"
        }
        else{
            "override_main"
        }
    }

    /**
     * Return the arguments for the class generacted by breed.
     *
     * @return	String of arguments
     */
    def getClassArguments(breed: Breed): String = {
        breed.className match {
            case "Observer" => "(val DEFAULT_BOARD_X: Int, val DEFAULT_BOARD_Y: Int)"
            case other => f"(val DEFAULT_observer: Observer, val DEFAULT_X: Int, val DEFAULT_Y: Int, val $initerVariableName: Int)"
        }
    }
}