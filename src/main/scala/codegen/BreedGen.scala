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
    val sourceLogName = "DEFAULT_source_logs"
    val workerParentName = "DEFAULT_Parent"
    val observerVariable = "DEFAULT_observer"
    val mainFunctionName = "main"
    val killWorkerFunctionName = "DEFAULT_kill_worker"
    val isWorkerDoneFunctionName = "DEFAULT_is_worker_done"

    /**
     * Generate Breed Class.
     *
     * @param	breed	
     * @param	context: Context for code generation
     * @return	ClassFile
     */
    def generateBreed(breed: Breed)(implicit context: Context): List[ClassFile] = {
        if (breed == context.getAgentBreed()){
            List(generateBreedClass(breed))
        }
        else{
            List(generateBreedClass(breed), generateBreedWorkerClass(breed))
        }   
    }


    /**
      * Generate Breed Class.
      *
      * @param breed: Breed for which to generate the  class
      * @param context: Context for the generation
      * @return
      */
    def generateBreedClass(breed: Breed)(implicit context: Context): ClassFile = {
        ClassFile(
            CodeGen.imports,
            "@lift",
            "class",
            breed.className,
            getParents(breed),
            generateBreedFields(breed), 
            generateBreedFunctions(breed)
        )
    }

    /**
     * Generate Class that allow breed to have nested ask
     * 
     * @param breed: Breed for which to generate class
     * @param context: Context for the generation
     * 
     * @return	ClassFile
     */
    def generateBreedWorkerClass(breed: Breed)(implicit context: Context): ClassFile = {
        ClassFile(
            CodeGen.imports,
            "@lift",
            "class",
            "WORKER_"+breed.className,
            List(breed.className),
            generateWorkerFields(breed),
            List(generateMainWorkerFunction(breed), generateWorkerIsDone(), generatekillWorkerFunction())
        )
    }


    /**
     * Generate Field for breed Class.
     *
     * @param	breed	
     * @param context: Context for the generation
     * @return	List[Instruction] 
     */
    def generateBreedFields(breed: Breed)(implicit context: Context):List[Instruction] = {
        if (breed == context.getObserverBreed()){
            ObserverGen.generateObserverSets():::
            ObserverGen.generateObserverFields():::
            ContentGen.generateVariables(breed)
        } else {
            ContentGen.generateVariables(breed)
        }
    }

    /**
     * Generate Functions for a Breed.
     *
     * @param	breed	
     * @param context: Context for the generation
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
     * Generate the main function for a breed.
     * 
     * @param	breed	
     * @param context: Context for the generation
     * @return	FunctionGenerator for the main function of the breed
     */
    def generateMainFunction(breed: Breed)(implicit context: Context): FunctionGen = {
        val init = if (breed == context.getObserverBreed()){
            InstructionGen("setup()")
        }
        else{
            generateSetupFunctionSwitch(breed)
        }
        val go = if (breed == context.getObserverBreed()){
            val fct = breed.getFunction("go").asInstanceOf[LinkedFunction]
            InstructionList(ContentGen.generate(fct.symTree)(fct, breed, context, Flag.ObserverMainFunctionFlag).asInstanceOf[InstructionBlock].content)
        }
        else{
            InstructionGen("")
        }
        val isScalaOverride = breed != context.getAgentBreed()

        FunctionGen(mainFunctionName, List(), "Unit", 
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
            ),
            0, 
            isScalaOverride  
        )
    }

    /**
      * Generate the main function switch for a breed.
      *
      * @param breed: Breed for which to generate the main function switch
      * @param context: Context for the generation
      * @return Instruction
      */
    def generateMainFunctionSwitch(breed: Breed)(implicit context: Context): Instruction = {
        if (breed == context.getObserverBreed() || breed.getAllAskedFunctions().isEmpty){
            EmptyInstruction
        }
        else{
            ToolGen.generateSwitchInstruction(
                breed, 
                askVaraibleName, 
                breed.getAllAskedFunctions().map(_.asInstanceOf[LinkedFunction]), Flag.MainFunctionFlag,
                InstructionGen(f"$askVaraibleName = -2")
            )
        }
    }

    /**
      * Generate the setup function switch for a breed.
      *
      * @param breed: Breed for which to generate the setup function switch
      * @param context: Context for the generation
      * @return Instruction
      */
    def generateSetupFunctionSwitch(breed: Breed)(implicit context: Context): Instruction = {
        if (breed == context.getObserverBreed() || breed.getAllCreateFunctions().isEmpty){
            EmptyInstruction
        }
        else{
            ToolGen.generateSwitchInstruction(breed, initerVariableName, breed.getAllCreateFunctions().map(_.asInstanceOf[LinkedFunction]), Flag.MainFunctionFlag)
        }
    }

    /**
      * Get the parents for a breed.
      *
      * @param breed: Breed for which to get the parents
      * @param context: context for the generation
      * @return List[String]: List of parents
      */
    def getParents(breed: Breed): List[String] = {
        if (breed.parent == null){
            List("Actor")
        }
        else{
            List(breed.parent.className)
        }
    }

    /**
      * Get the number of parents for a breed.
      *
      * @param breed: Breed for which to get the number of parents
      * @return Int: number of parents
      */
    def getNumberOfParent(breed: Breed, acc: Int = 0): Int = {
        if (breed.parent == null){
            acc
        }
        else{
            getNumberOfParent(breed.parent, acc+1)
        }
    }
    
    /**
      * Generate the ask lambda function.
      *
      * @param breed: breed for which to generate the ask lambda function
      * @param function: function for which to generate the ask lambda function
      * @param context: Context for the generation
      * @return FunctionGen: FunctionGen for the ask lambda function
      */
    def generateAskLambda(breed: Breed, function: LinkedFunction)(implicit context: Context): FunctionGen = {
        FunctionGen(Renamer.toValidName(function.name), function._args, Type.toString(function.getType()), 
                InstructionBlock(
                    InstructionGen(f"${askVaraibleName} = ${function.lambdaIndex}")
                )
        )
    }

    /**
     * Generate Field Updater that don't Updating the Map
     * 
      * @param	breed
      *
      * @param context
      * @return FunctionGen
      */
    def generateUpdaterFromParent(breed: Breed)(implicit context: Context): FunctionGen = {
        val p = "\""
        val vari = new Variable("dic")
        vari.setType(CodeGenType(logsType))

        FunctionGen("DEFAULT_UpdateFromParent", List(vari), "Unit",
        InstructionBlock(
            InstructionCompose("dic.map(kv => ",InstructionBlock(
            breed.getAllVariablesFromTree().map(x => InstructionCompose(f"if(kv._1 == $p${x.name}$p)",InstructionBlock(InstructionGen(f"${x.name} = kv._2.asInstanceOf[${Type.toString(x.getType())}]")))).toList
            ),")"
        )), 
        getNumberOfParent(breed))
    }

    /**
      * Generate Field Updater that also Update the Map
      *
      * @param breed
      * @param context
      * @return FunctionGen
      */
    def generateUpdaterFromWorker(breed: Breed)(implicit context: Context): FunctionGen = {
        val p = "\""
        val vari = new Variable("dic")
        vari.setType(CodeGenType(logsType))

        FunctionGen("DEFAULT_UpdateFromWorker", List(vari), "Unit",
        InstructionBlock(
            InstructionCompose("dic.map(kv => ",InstructionBlock(
            breed.getAllVariablesFromTree().map(x => InstructionCompose(f"if(kv._1 == $p${x.name}$p)",InstructionBlock(InstructionGen(x.getSetter(f"kv._2.asInstanceOf[${Type.toString(x.getType())}]"))))).toList
            ),")"
        ))
        , getNumberOfParent(breed))
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
     * @param	breed	
     * @return	FunctionGenerator for the main function of the breed
     */
    def generateMainWorkerFunction(breed: Breed)(implicit context: Context): FunctionGen = {
        FunctionGen(mainFunctionName, List(), "Unit", 
            InstructionBlock(
                InstructionGen(f"DEFAULT_UpdateFromParent($sourceLogName)"),
                InstructionCompose(f"while(true)", 
                    InstructionBlock(
                        InstructionGen("handleMessages()"),
                        generateMainFunctionSwitch(breed),
                        InstructionGen(f"asyncMessage(() => $workerParentName.DEFAULT_UpdateFromWorker($logName))"),
                        InstructionGen(f"while($askVaraibleName == -2) waitAndReply(1)"),
                        InstructionGen("waitLabel(Turn, 1)")
                    )
                )
            ),
            0,
            true  
        )
    }

    /**
     * Generate the function that tell if the Worker is done
     * 
     * @param	breed
     * @return	FunctionGenerator
     */
    def generateWorkerIsDone(): FunctionGen={
        FunctionGen(isWorkerDoneFunctionName, List(), "Boolean", InstructionGen(f"$askVaraibleName == -2"))
    }

    /**
     * Generate the function that kill the worker
     * 
     * @param	breed
     * @return	FunctionGenerator
     */
    def generatekillWorkerFunction(): FunctionGen={
        FunctionGen(killWorkerFunctionName, List(), "Unit", InstructionBlock())
    }

    /**
     * Generate the function that kill the worker
     * 
     * @param	breed
     * @return	FunctionGenerator
     */
    def generateWorkerFields(breed: Breed): List[Instruction]={
        List(
            InstructionGen(f"var $workerParentName: ${breed.className} = null"),
            InstructionGen(f"var $sourceLogName: $logsType = null")
        )
    }
}