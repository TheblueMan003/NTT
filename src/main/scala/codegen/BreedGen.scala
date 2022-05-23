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

object BreedGen{
    val initerVariableName = "DEFAULT_INITER"
    val askVaraibleName = "DEFAULT_ASK"
    val logsType = "mutable.Map[String, Any]"
    val logName = "DEFAULT_logs"
    val workerParentName = "DEFAULT_Parent"
    val observerVariable = "DEFAULT_observer"

    /**
     * Generate Breed Class.
     *
     * @param	breed	
     * @return	ClassFile
     */
    def generateBreed(breed: Breed)(implicit context: Context): List[ClassFile] = {
        List(
            ClassFile(
                CodeGen.imports,
                "@lift",
                "class",
                breed.className,
                getClassArguments(breed),
                getParents(breed),
                generateBreedFields(breed), 
                generateBreedFunctions(breed)
            ),
            ClassFile(
                CodeGen.imports,
                "@lift",
                "class",
                "WORKER_"+breed.className,
                getWorkerClassArguments(breed),
                List(breed.className+f"($observerVariable, 0, 0, -1)"),
                List(),
                List(generateMainWorkerFunction(breed))
            )
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
            breed.getAllAskedFunctions().map(f => generateAskLambda(breed, f.asInstanceOf[LinkedFunction])).toList:::
            List(generateUpdaterFromParent(breed), generateUpdaterFromWorker(breed)):::
        breed.getAllNormalFunctions().filter(_.name != "go").map{
            _ match {
                case lc: LinkedFunction => {
                    val flag = if (lc.functionType == netlogo.FunctionType.Normal){
                        Flag.FunctionFlag
                    } else{
                        Flag.MainFunctionFlag
                    }
                    ContentGen.generate(lc, breed, flag)
                }
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
            InstructionList(ContentGen.generate(fct.symTree)(fct, breed, context, Flag.ObserverMainFunctionFlag).asInstanceOf[InstructionBlock].content)
        }
        else{
            InstructionGen("")
        }

        FunctionGen(getMainFunctionName(breed), List(), "Unit", 
            InstructionBlock(
                init,
                InstructionCompose(f"while(true)", 
                    InstructionBlock(
                        go,
                        InstructionGen("handleMessages()"),
                        generateMainFunctionSwitch(breed),
                        InstructionGen("waitLabel(Turn, 1)")
                    )
                )
            )  
        )
    }

    def generateMainFunctionSwitch(breed: Breed)(implicit context: Context): Instruction = {
        if (breed == context.getObserverBreed() || breed.getAllAskedFunctions().isEmpty){
            EmptyInstruction
        }
        else{
            ToolGen.generateSwitchInstruction(breed, askVaraibleName, breed.getAllAskedFunctions().map(_.asInstanceOf[LinkedFunction]), Flag.MainFunctionFlag)
        }
    }

    def generateSettupFunctionSwitch(breed: Breed)(implicit context: Context): Instruction = {
        if (breed == context.getObserverBreed() || breed.getAllCreateFunctions().isEmpty){
            EmptyInstruction
        }
        else{
            ToolGen.generateSwitchInstruction(breed, initerVariableName, breed.getAllCreateFunctions().map(_.asInstanceOf[LinkedFunction]), Flag.MainFunctionFlag)
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
        else if (breed == context.getObserverBreed()){
            List()
        }
        else{
            List(breed.parent.className+f"($observerVariable, DEFAULT_X, DEFAULT_Y, $initerVariableName)")
        }
    }
    
    def generateAskLambda(breed: Breed, function: LinkedFunction)(implicit context: Context): FunctionGen = {
        FunctionGen(Renamer.toValidName(function.name), function._args, Type.toString(function.getType()), 
                InstructionBlock(
                    InstructionGen(f"${askVaraibleName} = ${function.lambdaIndex}")
                )
        )
    }

    def generateUpdaterFromParent(breed: Breed)(implicit context: Context): FunctionGen = {
        val p = "\""
        val vari = new Variable("dic")
        vari.setType(CodeGenType(logsType))

        FunctionGen("DEFAULT_UpdateFromParent", List(vari), "Unit",
        InstructionBlock(
            InstructionCompose("dic.map(kv => ",InstructionBlock(
            breed.getAllVariablesFromTree().map(x => InstructionCompose(f"if(kv._1 == $p${x.name}$p)",InstructionBlock(InstructionGen(f"${x.name} = kv._2.asInstanceOf[${Type.toString(x.getType())}]")))).toList
            ),")"
        )))
    }
    def generateUpdaterFromWorker(breed: Breed)(implicit context: Context): FunctionGen = {
        val p = "\""
        val vari = new Variable("dic")
        vari.setType(CodeGenType(logsType))

         FunctionGen("DEFAULT_UpdateFromParent", List(vari), "Unit",
        InstructionBlock(
            InstructionCompose("dic.map(kv => ",InstructionBlock(
            breed.getAllVariablesFromTree().map(x => InstructionCompose(f"if(kv._1 == $p${x.name}$p)",InstructionBlock(InstructionGen(x.getSetter(f"kv._2.asInstanceOf[${Type.toString(x.getType())}]"))))).toList
            ),")"
        )))
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
            case other => f"(val $observerVariable: Observer, val DEFAULT_X: Int, val DEFAULT_Y: Int, val $initerVariableName: Int)"
        }
    }

    /**
     * Return the arguments for the class generacted by breed.
     *
     * @return	String of arguments
     */
    def getWorkerClassArguments(breed: Breed): String = {
        f"(val $observerVariable: Observer, val $workerParentName: ${breed.className}, val $logName: $logsType, val $askVaraibleName: Int)"
    }

    /**
     * @param	breed	
     * @return	FunctionGenerator for the main function of the breed
     */
    def generateMainWorkerFunction(breed: Breed)(implicit context: Context): FunctionGen = {
        FunctionGen(getMainFunctionName(breed), List(), "Unit", 
            InstructionBlock(
                InstructionGen(f"DEFAULT_UpdateFromParent($logName)"),
                InstructionCompose(f"while(true)", 
                    InstructionBlock(
                        InstructionGen("handleMessages()"),
                        generateMainFunctionSwitch(breed),
                        InstructionGen("default_is_done = true"),
                        InstructionGen("waitLabel(Turn, 1)")
                    )
                )
            )  
        )
    }
}