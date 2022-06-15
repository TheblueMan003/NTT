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
    val askStack = "DEFAULT_ASK_STACK"
    val mainFunctionName = "main"
    val killWorkerFunctionName = "DEFAULT_kill_worker"
    val isWorkerDoneFunctionName = "DEFAULT_is_worker_done"
    val predicateFunctionName = "DEFAULT_PREDICATE_"
    val sorterFunctionName = "DEFAULT_SORTER_"
    val updateFromParentFunctionName = "DEFAULT_UpdateFromParent"
    val updateFromWorkerFunctionName = "DEFAULT_UpdateFromWorker"
    val myselfVariableName = "DEFAULT_myself"

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
    def generateBreedFields(breed: Breed)(implicit context: Context):List[Generator] = {
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
            generatePredicate(breed):::
            generateSorter(breed):::
            List(generateUpdaterFromParent(breed), generateUpdaterFromWorker(breed))
    }

    /**
     * Generate Predication function for breed.
     * Return this if true otherwise null
     * /
     * @param breed: Breed for which to generate the function
     * @param context: Context for the generation
     * @return	FunctionGen
     */
    def generatePredicate(breed: Breed)(implicit context: Context): List[FunctionGen] = {
        breed.predicateFilter.zipWithIndex.map{case (p, i) => {
            val fakeFunction = LinkedFunction("dummy", List(), ast.AST.Empty, breed, true)
            FunctionGen(f"$predicateFunctionName$i", List(), breed.className, InstructionBlock(
                ContentGen.generate(
                    SymTree.IfElseBlock(
                        List((p, SymTree.DirectValue("this"))), 
                        SymTree.DirectValue("null")
                    )
                )(fakeFunction, breed, context, Flag.FunctionFlag), 
            ))
        }
        }.toList
    }

    /**
     * Generate Sorter function for breed.
     * Return (value_to_sort_on, this)
     * /
     * @param breed: Breed for which to generate the function
     * @param context: Context for the generation
     * @return	FunctionGen
     */
    def generateSorter(breed: Breed)(implicit context: Context): List[FunctionGen] = {
        breed.predicateSorter.zipWithIndex.map{case (p, i) => {
            val fakeFunction = LinkedFunction("dummy", List(), ast.AST.Empty, breed, true)
            val generated = ContentGen.generateExpr(p)(fakeFunction, breed, context)

            FunctionGen(f"$sorterFunctionName$i", List(), f"(${p.getType()}, ${breed.className})", InstructionBlock(
                    generated._1,
                    InstructionGen(f"(${generated._2}, this)")
                ), 
            )
        }
        }.toList
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
            val setupInstr = if(context.getObserverBreed().hasFunction("setup")){
                val setupfct = context.getObserverBreed().getFunction("setup")
                ContentGen.generate(setupfct.asInstanceOf[LinkedFunction].symTree)(setupfct, breed, context, Flag.MainFunctionFlag)
            }else{
                EmptyInstruction
            }
            val defaultSetupInstr = if(context.getObserverBreed().hasFunction("default_setup")){
                val setupfct = context.getObserverBreed().getFunction("default_setup")
                ContentGen.generate(setupfct.asInstanceOf[LinkedFunction].symTree)(setupfct, breed, context, Flag.MainFunctionFlag)
            }else{
                EmptyInstruction
            }
            InstructionList(
                InstructionGen(f"$observerVariable = this"),
                InstructionGen(f"max_pxcor = ${ObserverGen.boardSizeX}"),
                InstructionGen(f"max_pycor = ${ObserverGen.boardSizeY}"),
                InstructionGen(f"min_pxcor = 0"),
                InstructionGen(f"min_pycor = 0"),
                defaultSetupInstr.asInstanceOf[InstructionBlock].toInstructionList(),
                setupInstr.asInstanceOf[InstructionBlock].toInstructionList()
            )
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
        val overrides = getOverrides(breed)
        val isScalaOverride = breed != context.getAgentBreed()

        FunctionGen(mainFunctionName, List(), "Unit", 
            InstructionBlock(
                overrides,
                init,

                InstructionCompose(f"while(true)", 
                    InstructionBlock(
                        go,
                        InstructionGen("handleMessages()"),
                        InstructionGen("waitLabel(Turn, 1)")
                    )
                )
            ),
            0, 
            isScalaOverride  
        )
    }

    /**
      * Get the overrides method name for the breed
      *
      * @param breed: Breed for which to get the overrides method name
      * @param context: Context for the generation
      * @return Instruction
      */
    def getOverrides(breed: Breed)(implicit context: Context): Instruction = {
        val p = "\""
        if (breed == context.getAgentBreed()){
            InstructionGen("")
        }
        else{
            InstructionGen(f"markOverride($p$updateFromParentFunctionName$p, $p$updateFromWorkerFunctionName$p)")
        }
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
                breed.getAllFunctions().map(_.asInstanceOf[LinkedFunction]), Flag.MainFunctionFlag,
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

        if (breed.getAllVariablesFromTree().isEmpty){
            FunctionGen(updateFromParentFunctionName, List(vari), "Unit",InstructionBlock(), getNumberOfParent(breed))
        }
        else{
            FunctionGen(updateFromParentFunctionName, List(vari), "Unit",
            InstructionBlock(
                InstructionGen("var values = dic.toList"),
                InstructionCompose("while(!values.isEmpty)",InstructionBlock(
                    InstructionGen("var kv = values.head"),
                    InstructionGen("values = values.tail"),
                    InstructionList(
                        breed.getAllVariablesFromTree().map(x => InstructionCompose(f"if(kv._1 == $p${Renamer.toValidName(x.name)}$p)",InstructionBlock(InstructionGen(f"${Renamer.toValidName(x.name)} = kv._2.asInstanceOf[${Type.toString(x.getType())}]")))).toList
                    )
                )
            )), 
            getNumberOfParent(breed))
        }
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

        if (breed.getAllVariablesFromTree().isEmpty){
            FunctionGen(updateFromWorkerFunctionName, List(vari), "Unit",InstructionBlock(), getNumberOfParent(breed))
        }
        else{
            FunctionGen(updateFromWorkerFunctionName, List(vari), "Unit",
            InstructionBlock(
                InstructionGen("var values = dic.toList"),
                InstructionCompose("while(!values.isEmpty)",InstructionBlock(
                    InstructionGen("var kv = values.head"),
                    InstructionGen("values = values.tail"),
                    InstructionList(
                        breed.getAllVariablesFromTree().map(x => InstructionCompose(f"if(kv._1 == $p${Renamer.toValidName(x.name)}$p)",InstructionBlock(
                            InstructionGen(f"${Renamer.toValidName(x.name)} = kv._2.asInstanceOf[${Type.toString(x.getType())}]"),
                            InstructionGen(f"${BreedGen.logName}($p${x.getName()}$p) = ${Renamer.toValidName(x.name)}")
                        ))).toList
                    )
                )
            ))
            , getNumberOfParent(breed))
        }
    }

    /**
     * @param	breed	
     * @return	FunctionGenerator for the main function of the breed
     */
    def generateMainWorkerFunction(breed: Breed)(implicit context: Context): FunctionGen = {
        FunctionGen(mainFunctionName, List(), "Unit", 
            InstructionBlock(
                InstructionGen(f"DEFAULT_UpdateFromParent($workerParentName.get_$logName())"),
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
        FunctionGen(killWorkerFunctionName, List(), "Unit", InstructionBlock(InstructionGen("deleted = true")))
    }

    /**
     * Generate the function that kill the worker
     * 
     * @param	breed
     * @return	FunctionGenerator
     */
    def generateWorkerFields(breed: Breed): List[Instruction]={
        List(
            InstructionGen(f"var $workerParentName: Agent = null")
        )
    }
}