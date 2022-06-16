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
import java.nio.file.Path
import netlogo.Types
import analyser.TypeChecker
import analyser.NameAnalyser

object ContentGen{
    var varCounter = 0

    /**
     * Generate Function Content.
     *
     * @param	function
     * @param   breed
     * @return	FunctionGen
     */
    def generate(function: LinkedFunction, breed: Breed, flag: Flag)(implicit context: Context): FunctionGen = {
        FunctionGen(Renamer.toValidName(function.name), function._args, Type.toString(function.getType()), generate(function.symTree)(function, breed, context, flag))
    }

    /**
     * Generate Variables.
     *
     * @param	variables	
     * @return	List[Instruction]
     */
    def generateVariables(breed: Breed)(implicit context: Context): List[Generator] = {
        val variables = breed.getAllVariables()
        val exported = variables.filter(_.exported)
        generateDefaultVariable(breed) ::: 
        exported.map(generate(_)).toList ::: 
        exported.map(generateGetterSetter(_)).toList.flatten
    }

    /**
     * Generate Default Variables.
     *
     * @param	breed	
     * @return	List[Instruction]
     */
    def generateDefaultVariable(breed: Breed)(implicit context: Context): List[Generator] = {
        if (breed == context.getAgentBreed()){
            List(
                InstructionGen(f"val ${BreedGen.logName} = mutable.Map[String, Any]()"),
                FunctionGen(f"get_${BreedGen.logName}", List(), "mutable.Map[String, Any]", InstructionGen(BreedGen.logName)),
                InstructionGen(f"var ${BreedGen.askVaraibleName} = -1"),
                InstructionGen(f"var ${BreedGen.initerVariableName} = -1"),
                InstructionGen(f"var ${BreedGen.observerVariable}: Observer = null"),
                InstructionGen(f"var ${BreedGen.myselfVariableName}: Agent = null"),
                InstructionGen(f"var ${BreedGen.askStack}: List[Agent] = List()"),
            )
        }
        else{
            List()
        }
    }

    /**
     * Generate Getter and Setter for variable.
     *
     * @param	variable: Variable for which to generate the setter
     * @return	List[Instruction] containing setter and getter
     */
    def generateGetterSetter(variable: Variable)(implicit context: Context): List[Instruction] = {
        val typ = Type.toString(variable.getType())
        val p = "\""
        variable.hasGetterSetter = true
        List(
        InstructionGen(f"def ${variable.getGetterName()}(): ${typ} = ${variable.getName()}"),
        InstructionCompose(f"def ${variable.getSetterName()}(DEFAULT_value : ${typ}): Unit = ", 
            InstructionBlock(
                InstructionGen(f"${BreedGen.logName}($p${variable.getName()}$p) = DEFAULT_value"),
                InstructionGen(f"${variable.getName()} = DEFAULT_value"))
            )
        )
    }

    /**
     * Generate Variable.
     *
     * @param	variable
     * @return	Instruction
     */
    def generate(variable: Variable)(implicit context: Context): Instruction = {
        val typ = variable.getType()
        InstructionGen(f"var ${Renamer.toValidName(variable.name)} : ${Type.toString(typ)} = ${Type.defaultValue(typ)}")
    }

    /**
     * Convert SymTree Instruction to Scala Instruction
     *
     * @param	mixed	inst: SymTree	
     * @return	Instruction
     */
    def generate(instr: SymTree)(implicit function: Function, breed: Breed, context: Context, flag: Flag): Instruction = {
        val isInMain = function.name == "go"

        instr match{
            case Block(body) => {
                val content = body.map(generate(_))
                InstructionBlock(content)
            }
            case Declaration(VariableValue(v), value) => {
                val expr = generateExpr(value)
                val tmp = getUniqueVariableName()
                InstructionList(InstructionList(expr._1, InstructionGen(f"val $tmp = ${expr._2}")), InstructionGen(f"${v.getSetter(tmp)}"))
            }
            case Assignment(VariableValue(v), value) => {
                val expr = generateExpr(value)
                val tmp = getUniqueVariableName()
                if (context.getObserverBreed() != breed && v.isGlobal){
                    InstructionList(InstructionList(expr._1, InstructionGen(f"val $tmp = ${expr._2}")), InstructionGen(f"${BreedGen.observerVariable}.${v.getSetter(tmp)}"))
                }
                else{
                    InstructionList(InstructionList(expr._1, InstructionGen(f"val $tmp = ${expr._2}")), InstructionGen(f"${v.getSetter(tmp)}"))
                }
            }
            case Assignment(OtherVariable(v, index), value) => {
                val expr = generateExpr(value)
                InstructionList(expr._1, InstructionGen(f"${BreedGen.askStack}($index).asInstanceOf[${v.breeds.head.className}].${v.getSetter(expr._2)}"))
            }
            case Call(fct, args) => {
                val (prefix, content) = generateExpr(instr.asInstanceOf[SymTree.Expression])
                InstructionList(prefix, InstructionGen(content))
            }
            case CreateBreed(b, nb, fct) => {
                val tmp = getUniqueVariableName()
                generateRepeat(nb, InstructionBlock(
                    InstructionGen(f"val $tmp = new ${b.name.className}()"),
                    InstructionGen(f"$tmp.${BreedGen.observerVariable} = ${BreedGen.observerVariable}"),
                    InstructionGen(f"$tmp.${BreedGen.initerVariableName} = ${fct.lambdaIndex}"),
                    InstructionGen(f"${Renamer.toValidName(b.name.pluralName)}.add($tmp)")
                ))
            }
            case HatchBreed(b, nb, fct) => {
                val tmp = getUniqueVariableName()
                generateRepeat(nb, InstructionBlock(
                    InstructionGen(f"val $tmp = new ${b.name.className}()"),
                    InstructionGen(f"$tmp.${BreedGen.observerVariable} = ${BreedGen.observerVariable}"),
                    InstructionGen(f"$tmp.${BreedGen.initerVariableName} = ${fct.lambdaIndex}"),
                    InstructionGen(f"${BreedGen.observerVariable}.get_${Renamer.toValidName(b.name.pluralName)}().add($tmp)"),
                    InstructionGen(f"$tmp.${BreedGen.updateFromParentFunctionName}(${BreedGen.logName})"),
                ))
            }
            case DirectValue(v) => InstructionGen(v.toString())

            case IfBlock(cond, block) => {
                val condGen = generateExpr(cond)
                InstructionList(condGen._1, InstructionCompose(f"if(${condGen._2})", generate(block)))
            }
            case IfElseBlock(conds, block) => {
                conds match {
                    case head :: Nil => generateIfElse(head._1, head._2, block)
                    case head :: tail => generateIfElse(head._1, head._2, IfElseBlock(tail, block))
                    case Nil => generate(block)
                }
            }

            case Loop(block) => InstructionCompose("while(true)", generate(block))
            case Repeat(number, block) => {
                generateRepeat(number, generate(block))
            }
            case While(cond, block) => {
                val expr = generateExpr(cond)
                InstructionList(
                    expr._1,
                    InstructionCompose(f"while(${expr._2})", generate(block))
                )
            }

            case Report(expr) => {
                if (!function.hasReturnValue)
                    Reporter.warning(f"${function.name} is not marked as to-report but yet has a report instruction.")
                val expr2 = generateExpr(expr)
                val vari = getUniqueVariableName()
                InstructionList(
                    expr2._1,
                    InstructionGen(function.returnVariable.getSetter(expr2._2))
                )
            }
            case Ask(upperCaller, b, turtles, block) => {
                val set = generateExpr(turtles)
                val agents = getUniqueVariableName()

                val doneMSG = getUniqueVariableName()
                val isDone = getUniqueVariableName()
                val worker = getUniqueVariableName()

                val setter = turtles.getType() match{
                    case BreedType(breed) => f"List(${set._2})"
                    case _ => f"NetLogoUtils.toList[${b.className}](${set._2})"
                }

                InstructionList(
                    set._1,
                    InstructionGen(f"var $agents = $setter"),
                    InstructionCompose(f"while($agents.nonEmpty)", InstructionBlock(
                        InstructionCompose(f"val $worker =", newWorker(f"$agents.head", block, b, List(), true)),
                        InstructionGen(f"var $isDone = false"),
                        InstructionCompose(f"while(!$isDone)", InstructionBlock(
                            InstructionGen(f"val $doneMSG = asyncMessage(() => $worker.${BreedGen.isWorkerDoneFunctionName}())"),
                            InstructionCompose(f"while(!$doneMSG.isCompleted)", InstructionBlock(
                                InstructionGen("waitAndReply(1)")
                            )),
                            InstructionGen(f"$isDone = $doneMSG.popValue.get")
                        )),
                        InstructionGen(f"asyncMessage(() => $worker.${BreedGen.killWorkerFunctionName}())"),
                        InstructionGen(f"$agents = $agents.tail")
                    ))
                )
            }
        }
    }

    /**
      * Return an instruction to generate a new worker for the object
      * 
      * @param source: Source Variable
      * @param breed: Breed of the object
      */
    def newWorker(source: String, function: Function, breed: Breed, assignmentList: List[String] = List(), realAsk: Boolean = false): Instruction = {
        val tmpvar = getUniqueVariableName()
        val fct = function.lambdaIndex

        InstructionBlock(
            InstructionGen(f"val $tmpvar = new WORKER_${breed.className}()"),
            InstructionGen(f"$tmpvar.${BreedGen.observerVariable} = ${BreedGen.observerVariable}"),
            InstructionGen(f"$tmpvar.${BreedGen.askVaraibleName} = $fct"),
            InstructionGen(f"$tmpvar.${BreedGen.workerParentName} = $source"),
            InstructionGen(f"$tmpvar.${BreedGen.myselfVariableName} = this"),
            if (realAsk){
                InstructionGen(f"$tmpvar.${BreedGen.askStack} = this::${BreedGen.askStack}")
            }
            else{
                InstructionGen(f"$tmpvar.${BreedGen.askStack} = ${BreedGen.askStack}")
            },
            InstructionList(assignmentList.map(x => InstructionGen(f"$tmpvar.$x"))),
            InstructionGen(f"$tmpvar")
        )
    }

    /**
      * Generate an instruction to call a function
      *
      * @param function: Function to call
      * @param breed: Breed of the function
      */
    def generateFunctionCallNoReturn(args: List[String], function: LinkedFunction, breed: Breed):Instruction = {
        val vari = getUniqueVariableName()
        val vari2 = getUniqueVariableName()
        val vari3 = getUniqueVariableName()

        val copyarg = function._args.zip(args).map{case (x,y)=>f"${Renamer.toValidName(x.name)} = $y"}

        InstructionList(
            InstructionCompose(f"val $vari = ", newWorker("this", function, breed, copyarg)),
            InstructionGen(f"var $vari2 = false"),
            InstructionCompose(f"while(!$vari2)", InstructionBlock(
                InstructionGen(f"val $vari3 = asyncMessage(() => $vari.${BreedGen.isWorkerDoneFunctionName}())"),
                InstructionCompose(f"while(!$vari3.isCompleted)", InstructionBlock(
                    InstructionGen("waitAndReply(1)")
                )),
                InstructionGen(f"$vari2 = $vari3.popValue.get == true")
            )),
            InstructionGen(f"$vari.${BreedGen.killWorkerFunctionName}()")
        )
    }

    /**
      * Generate an instruction for a if-else block
      */
    def generateIfElse(cond: Expression, ifBlock: SymTree, elseBlock: SymTree)(implicit function: Function, breed: Breed, context: Context, flag: Flag): Instruction = {
        val expr = generateExpr(cond)
        InstructionList(
            expr._1,
            InstructionCompose(f"if(${expr._2})", generate(ifBlock)),
            InstructionCompose(f"else", generate(elseBlock)),
        )
    }

    /**
      * Generate an instruction for a if-else expression
      */
    def generateIfElseExpr(cond: Expression, ifBlock: Expression, elseBlock: Expression)(implicit function: Function, breed: Breed, context: Context): (Instruction, String) = {
        val expr = generateExpr(cond)
        val iff = generateExpr(ifBlock)
        val elze = generateExpr(elseBlock)
        (expr._1,
            InstructionList(
            InstructionCompose(f"if(${expr._2})", InstructionBlock(iff._1, InstructionGen(iff._2))),
            InstructionCompose(f"else", InstructionBlock(elze._1, InstructionGen(elze._2)))
            ).generate(0),
        )
    }

    /**
      * Generate an instruction for a repeat expression
      */
    def generateRepeat(number: Expression, content: Instruction)(implicit function: Function, breed: Breed, context: Context):Instruction = {
        val expr = generateExpr(number)
        val vari = getUniqueVariableName()
        InstructionList(
            expr._1,
            InstructionGen(f"var $vari = 0"),
            InstructionCompose(f"while($vari < ${expr._2})", 
                InstructionBlock(content, InstructionGen(f"$vari = $vari + 1"))
            )
        )
    }

    /**
      * Generate an instruction to get position of the agent
      */
    def generateTurtlePositionGetter(turtle: Expression)(implicit function: Function, breed: Breed, context: Context): (Instruction, String) = {
        val expr = generateExpr(turtle)
        val vari = getUniqueVariableName()
        val elm = getUniqueVariableName()
        val ret = getUniqueVariableName()
        val typ = TypeChecker.getBreedFrom(turtle).head.className
        (
            InstructionList(
                expr._1,
                InstructionGen(f"var $vari = ${expr._2}"),
                InstructionGen(f"var $ret = List[($typ,Int, Int)]()"),
                InstructionCompose(f"while(!$vari.isEmpty)", InstructionBlock(
                    InstructionGen(f"val $elm = $vari.head"),
                    InstructionGen(f"$vari = $vari.tail"),
                    InstructionGen(f"$ret = $ret:::List(($elm, $elm.get_pxcor(), $elm.get_pycor()))")
                ))
            ), 
            f"$ret"
        )
    }

    /**
      * Generate an instruction to fill a list of turtles
      *
      * @param expr: Turtles
      * @param predicate: Index of the predicate Function
      * @return	Scala expression as a (Instruction, String) Instruction need to be used befor the String
      */
    def generateWithExpression(expr: Expression, predicate: Int)(implicit function: Function, breed: Breed, context: Context): (Instruction, String) = {
        val expr2 = generateExpr(expr)
        val vari = getUniqueVariableName()
        val result = getUniqueVariableName()
        (InstructionList(
            expr2._1, 
            InstructionGen(f"val $vari = ${expr2._2}.toList.map(s => asyncMessage(() => s.${BreedGen.predicateFunctionName}$predicate()))"),
            InstructionCompose(f"while(!$vari.forall(_.isCompleted))", InstructionBlock(
                InstructionGen("waitAndReply(1)")
            )),
            InstructionGen(f"val $result = $vari.map(o => o.popValue.get).filter(_ != null)")
        ),
        result)
    }

    /**
     * Convert Sym.Tree Expression to Scala String Expression
     *
     * @param	exp: Expression
     * @return	Scala expression as a (Instruction, String)
     */
    def generateExpr(expr: Expression)(implicit function: Function, breed: Breed, context: Context): (Instruction, String) = {
        expr match{
            case VariableValue(v) => {
                if (context.getObserverBreed() != breed && v.isGlobal){
                    (EmptyInstruction, f"${BreedGen.observerVariable}.${v.getGetter()}")
                }
                else{
                    (EmptyInstruction, f"${v.getGetter()}")
                }
            }
            case OtherVariable(v, index) => {
                (EmptyInstruction, f"${BreedGen.askStack}($index).asInstanceOf[${v.breeds.head.className}].${v.getGetter()}")
            }
            case BooleanValue(v) => (EmptyInstruction, v.toString())
            case IntValue(v) => (EmptyInstruction, v.toString())
            case FloatValue(v) => (EmptyInstruction, v.toString())
            case StringValue(v) => (EmptyInstruction, "\"" + v.toString() + "\"")
            case OneOfValue(lst) => {
                val tmp = getUniqueVariableName()
                val values = lst.map(x => generateExpr(x))
                val instructions = values.map(x => x._1)
                val lstElemt = values.map(x => x._2).reduce((a, b) => f"$a, $b")
                (
                    InstructionList(
                        InstructionList(instructions),
                        InstructionGen(f"val $tmp = List($lstElemt)")
                    ),
                    f"$tmp(Random.nextInt($tmp.size))"
                )
            }
            case Not(value) => {
                val expr = generateExpr(value)
                (expr._1, f"!(${expr._2})")
            }


            case BreedValue(b) => {
                if (breed == context.getObserverBreed()){
                    (EmptyInstruction, Renamer.toValidName(b.pluralName))
                }
                else{
                    (EmptyInstruction, f"${BreedGen.observerVariable}.${Renamer.toGetterNane(b.pluralName)}()")
                }
            }
            case DirectValue(v) => (EmptyInstruction, v.toString())
            case WithValue(expr, _, predicate) => {
                generateWithExpression(expr, predicate)
            }
            case SortBy(expr, _, predicate) => {
                val expr2 = generateExpr(expr)
                val vari = getUniqueVariableName()
                val result = getUniqueVariableName()
                (InstructionList(
                    expr2._1, 
                    InstructionGen(f"val $vari = ${expr2._2}.toList.map(s => asyncMessage(() => s.${BreedGen.sorterFunctionName}$predicate()))"),
                    InstructionCompose(f"while(!$vari.forall(_.isCompleted))", InstructionBlock(
                        InstructionGen("waitAndReply(1)")
                    )),
                    InstructionGen(f"val $result = $vari.map(o => o.popValue.get).sortBy(_._1).map(_._2)")
                ),
                result)
            }
            case MinOneAgent(value) => {
                val expr = generateExpr(value)
                (
                    expr._1,
                    f"${expr._2}.head"
                )
            }
            case MaxOneAgent(value) => {
                val expr = generateExpr(value)
                (
                    expr._1,
                    f"${expr._2}.last"
                )
            }
            case MinNAgent(value, nb) => {
                val expr = generateExpr(value)
                val nbgen = generateExpr(nb)
                (
                    InstructionList(expr._1, nbgen._1),
                    f"${expr._2}.take(${nbgen._2})"
                )
            }
            case MaxNAgent(value, nb) => {
                val expr = generateExpr(value)
                val nbgen = generateExpr(nb)
                (
                    expr._1,
                    f"${expr._2}.reverse.take(${nbgen._2})"
                )
            }
            case BreedAt(b, x, y) => {
                val xexpr = generateExpr(x)
                val yexpr = generateExpr(y)
                val breedGen = generateTurtlePositionGetter(b)
                val tmp1 = getUniqueVariableName()
                val tmp2 = getUniqueVariableName()
                (
                    InstructionList(
                        xexpr._1, 
                        yexpr._1, 
                        breedGen._1, 
                        InstructionGen(f"val $tmp1 = ${xexpr._2}"),
                        InstructionGen(f"val $tmp2 = ${yexpr._2}")),
                    f"NetLogoUtils.filterPosition(${breedGen._2}, $tmp1, $tmp2)"
                )
            }
            case BreedAtSingle(b, x, y) => {
                val xexpr = generateExpr(x)
                val yexpr = generateExpr(y)
                val breedGen = generateTurtlePositionGetter(b)
                val tmp1 = getUniqueVariableName()
                val tmp2 = getUniqueVariableName()
                (
                    InstructionList(
                        xexpr._1, 
                        yexpr._1, 
                        breedGen._1, 
                        InstructionGen(f"val $tmp1 = ${xexpr._2}"),
                        InstructionGen(f"val $tmp2 = ${yexpr._2}")),
                    f"NetLogoUtils.filterPosition(${breedGen._2}, $tmp1, $tmp2).head"
                )
            }
            case BreedOn(b, agent) => {
                val agentexpr = generateTurtlePositionGetter(agent)
                val breedGen = generateTurtlePositionGetter(b)
                val tmp = getUniqueVariableName()
                (
                    InstructionList(
                        agentexpr._1,
                        breedGen._1,
                        InstructionGen(f"val $tmp = ${agentexpr._2}.map(x => (x._2, x._3))")
                    ),
                    f"NetLogoUtils.filterPositions(${breedGen._2}, $tmp.toList)"
                )
            }
            case Neighbors => {
                val tmp1 = getUniqueVariableName()
                val tmp2 = getUniqueVariableName()
                val patches = SymTree.BreedValue(context.getPatchBreed())
                patches.setType(BreedSetType(context.getPatchBreed()))
                val breedGen = generateTurtlePositionGetter(patches)

                (
                     InstructionList(
                        breedGen._1,
                        InstructionGen(f"val $tmp1 = get_pxcor()"),
                        InstructionGen(f"val $tmp2 = get_pycor()")),
                      f"NetLogoUtils.filterPositions(${breedGen._2}, List(($tmp1 - 1, $tmp2), ($tmp1 + 1, $tmp2), ($tmp1, $tmp2 - 1), ($tmp1, $tmp2 + 1), ($tmp1 + 1, $tmp2 + 1), ($tmp1 - 1, $tmp2 - 1), ($tmp1 + 1, $tmp2 - 1), ($tmp1 - 1, $tmp2 + 1)))"
                )
            }
            case Other(b) => {
                val breedGen = generateExpr(b)
                (
                    breedGen._1,
                    f"${breedGen._2}.filter(DEFAULT_TURTLE => DEFAULT_TURTLE != this)"
                )
            }
            case OneOf(b) => {
                val breedGen = generateExpr(b)
                val tmp = getUniqueVariableName()
                (
                    InstructionList(breedGen._1, InstructionGen(f"val $tmp = ${breedGen._2}.toList")),
                    f"$tmp(Random.nextInt($tmp.size))"
                )
            }
            case Nobody => (EmptyInstruction,"null")


            case All(expr, _, predicate) => {
                val expr2 = generateExpr(expr)
                val vari = getUniqueVariableName()
                val result = getUniqueVariableName()
                (InstructionList(
                    expr2._1, 
                    InstructionGen(f"val $vari = ${expr2._2}.toList.map(s => asyncMessage(() => s.${BreedGen.predicateFunctionName}$predicate()))"),
                    InstructionCompose(f"while(!$vari.forall(_.isCompleted))", InstructionBlock(
                        InstructionGen("waitAndReply(1)")
                    )),
                    InstructionGen(f"val $result = $vari.map(o => o.popValue.get).filter(_ != null).toSet.subsetOf(${expr2._2}.toSet)")
                ),
                result)
            }
            case Any(expr) => {
                val gen = generateExpr(expr)
                return (gen._1, f"!(${gen._2}.isEmpty)")
            }
            case Count(expr) => {
                val gen = generateExpr(expr)
                return (gen._1, gen._2+".size")
            }
                
            case ListValue(lst) => ???

            case IfElseBlockExpression(conds, block) => {
                conds match {
                    case head :: Nil => generateIfElseExpr(head._1, head._2, block)
                    case head :: tail => generateIfElseExpr(head._1, head._2, IfElseBlockExpression(tail, block))
                    case Nil => generateExpr(block)
                }
            }

            case BinarayExpr(op, lf, rt) => {
                val left = generateExpr(lf)
                val right = generateExpr(rt)
                (
                    InstructionList(left._1, right._1),
                    getOperator(op, left._2, right._2)
                )
            }

            case Call(fct, args) => {
                fct match{
                    case fct: LinkedFunction => {
                        if (!fct.hasReturnValue){
                            val argsPro = args.map(generateExpr(_))
                            val argsCall = argsPro.map(_._2)
                            val argsInstr = argsPro.map(_._1)
                            (InstructionList(InstructionList(argsInstr), generateFunctionCallNoReturn(argsCall, fct, breed)), "")
                        }
                        else{
                            val argsPro = args.map(generateExpr(_))
                            val argsCall = argsPro.map(_._2)
                            val argsInstr = argsPro.map(_._1)
                            (InstructionList(InstructionList(argsInstr), generateFunctionCallNoReturn(argsCall, fct, breed)), Renamer.toValidName(fct.returnVariable.name))
                        }
                    }
                    case fct: BaseFunction =>{
                        val argsPro = args.map(generateExpr(_))
                        val argsCall = argsPro.map(_._2)
                        val argsInstr = argsPro.map(_._1)
                        (
                            InstructionList(argsInstr),
                            InstructionGen(fct.call(argsCall)).generate(0)
                        )
                    }
                }
            }
            case Ask_For_Report(upperCaller, b, turtles, block) => {
                val set = generateExpr(turtles)
                val agents = getUniqueVariableName()

                val doneMSG = getUniqueVariableName()
                val isDone = getUniqueVariableName()
                val worker = getUniqueVariableName()

                val output = getUniqueVariableName()
                val tmp = getUniqueVariableName()

                val setter = turtles.getType() match{
                    case BreedType(breed) => f"List(${set._2})"
                    case _ => f"NetLogoUtils.toList[${b.className}](${set._2})"
                }
                val getter = expr.getType() match{
                    case ListType(_) => output
                    case _ => f"${output}.head"
                }

                (InstructionList(
                    set._1,
                    InstructionGen(f"var $agents = ${setter}"),
                    InstructionGen(f"var $output = List[${Type.toString(block.returnVariable.getType())}]()"),
                    InstructionCompose(f"while($agents.nonEmpty)", InstructionBlock(
                        InstructionCompose(f"val $worker =", newWorker(f"$agents.head", block, b, List(), true)),
                        InstructionGen(f"var $isDone = false"),
                        InstructionCompose(f"while(!$isDone)", InstructionBlock(
                            InstructionGen(f"val $doneMSG = asyncMessage(() => $worker.${BreedGen.isWorkerDoneFunctionName}())"),
                            InstructionCompose(f"while(!$doneMSG.isCompleted)", InstructionBlock(
                                InstructionGen("waitAndReply(1)")
                            )),
                            InstructionGen(f"$isDone = $doneMSG.popValue.get")
                        )),
                        InstructionGen(f"val $tmp = $worker.${block.returnVariable.getGetter}"),
                        InstructionGen(f"$output = $tmp :: $output"),
                        InstructionGen(f"asyncMessage(() => $worker.${BreedGen.killWorkerFunctionName}())"),
                        InstructionGen(f"$agents = $agents.tail")
                    ))
                ),
                getter
                )
            }
        }
    }

    /**
     * Convert NetLogo Operator to Scala Operator
     *
     * @param	op: String	
     * @return	Scala operator
     */
    def getOperator(op: String, left: String, right: String): String = {
        op match {
            case "and" => f"$left && $right"
            case "or" => f"$left || $right"
            case "=" => f"$left == $right"
            case "^" => f"Math.pow($left, $right)"
            case "xor" => f"$left || !$right && !$left || $right"
            case "mod" => f"Math.floorMod($left, $right)"
            case _ => f"$left $op $right"
        }
    }

    
    def getUniqueVariableName():String={
        val id = varCounter
        varCounter += 1
        f"tmp_$id"
    }
}