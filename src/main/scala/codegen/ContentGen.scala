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
    def generateVariables(variables: Iterable[Variable])(implicit context: Context): List[Instruction] = {
        val exported = variables.filter(_.exported)
        generateDefaultVariable() ::: 
        exported.map(generate(_)).toList ::: 
        exported.map(generateGetterSetter(_)).toList.flatten
    }

    def generateDefaultVariable(): List[Instruction] = {
        List(
            InstructionGen(f"val ${BreedGen.logName} = mutable.Map[String, Any]()"),
            InstructionGen(f"var ${BreedGen.askVaraibleName} = -1"),
            InstructionGen(f"var ${BreedGen.initerVariableName} = -1"),
            InstructionGen(f"var ${BreedGen.observerVariable}: Observer = null")
        )
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
                InstructionList(expr._1, InstructionGen(f"${v.getSetter(expr._2)}"))
            }
            case Assignment(VariableValue(v), value) => {
                val expr = generateExpr(value)
                InstructionList(expr._1, InstructionGen(f"${v.getSetter(expr._2)}"))
            }
            case Declaration(OfValue(v, b), value) => {
                val expr = generateExpr(value)
                val from = generateExpr(b)
                InstructionList(expr._1, from._1, InstructionGen(f"${from._2}.${v.getSetter(expr._2)}"))
            }
            case Assignment(OfValue(v, b), value) => {
                val expr = generateExpr(value)
                val from = generateExpr(b)
                println(instr)
                InstructionList(expr._1, from._1, InstructionGen(f"${from._2}.${v.getSetter(expr._2)}"))
            }
            case Call(fct, args) => {
                val (prefix, content) = generateExpr(instr.asInstanceOf[SymTree.Expression])
                InstructionList(prefix, InstructionGen(content))
            }
            case CreateBreed(b, nb, fct) => {
                val tmp = getUniqueVariableName()
                generateRepeat(nb, InstructionBlock(
                    InstructionGen(f"val $tmp = new ${b.name.className}()"),
                    InstructionGen(f"$tmp.${BreedGen.observerVariable} = this"),
                    InstructionGen(f"$tmp.${BreedGen.initerVariableName} = ${fct.lambdaIndex}"),
                    InstructionGen(f"${Renamer.toValidName(b.name.pluralName)}.add($tmp)")
                ))
            }

            case IfBlock(cond, block) => InstructionCompose(f"if(${generateExpr(cond)})", generate(block))
            case IfElseBlock(conds, block) => {
                conds match {
                    case head :: Nil => generateIfElse(head._1, head._2, block)
                    case head :: tail => generateIfElse(head._1, head._2, IfElseBlock(tail, block))
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
                    InstructionGen(f"val $vari = ${expr2._2}"),
                    InstructionGen(vari)
                )
            }

            case Tick => {
                if (function.name != "go"){
                    Reporter.warning("Tick command not supported outside of go method")
                }
                InstructionGen("waitLabel(Turn, 1)")
            }
            case Ask(upperCaller, turtles, block) => {
                val set = generateExpr(turtles)
                val vari = getUniqueVariableName()

                flag match{
                    case Flag.ObserverMainFunctionFlag => {
                        val fct = block.name
                        InstructionList(
                            set._1,
                            InstructionGen(f"val $vari = ${set._2}.toList.map(s => asyncMessage(() => s.$fct(this)))"),
                            InstructionCompose(f"while(!$vari.forall(_.isCompleted))", InstructionBlock(
                                InstructionGen("waitAndReply(1)")
                            ))
                        )
                    }
                    case Flag.MainFunctionFlag => {
                        val fct = block.lambdaIndex
                        val vari2 = getUniqueVariableName()
                        val vari3 = getUniqueVariableName()
                        val tmpvar = getUniqueVariableName()
                        InstructionList(
                            set._1,
                            InstructionCompose(f"val $vari = ${set._2}.toList.map(s => ",
                                InstructionBlock(
                                    InstructionGen(f"val $tmpvar = new WORKER_Turtle()"),
                                    InstructionGen(f"$tmpvar.${BreedGen.observerVariable} = ${BreedGen.observerVariable}"),
                                    InstructionGen(f"$tmpvar.${BreedGen.askVaraibleName} = $fct"),
                                    InstructionGen(f"$tmpvar.${BreedGen.workerParentName} = this"),
                                    InstructionGen(f"$tmpvar.${BreedGen.sourceLogName} = ${BreedGen.logName}"),
                                    InstructionGen(f"$tmpvar")
                                ),
                                ")"
                            ),
                            InstructionGen(f"var $vari3 = false"),
                            InstructionCompose(f"while(!$vari3)", InstructionBlock(
                                InstructionGen(f"val $vari2 = $vari.map(s => asyncMessage(() => s.${BreedGen.isWorkerDoneFunctionName}()))"),
                                InstructionCompose(f"while(!$vari2.forall(_.isCompleted))", InstructionBlock(
                                    InstructionGen("waitAndReply(1)")
                                )),
                                InstructionGen(f"$vari3 = $vari2.map(o => o.popValue.get).toList.forall(_ == true)")
                            )),
                            InstructionGen(f"$vari.map(s => asyncMessage(() => s.${BreedGen.isWorkerDoneFunctionName}()))"),
                        )
                    }
                }
            }
        }
    }

    def generateIfElse(cond: Expression, ifBlock: SymTree, elseBlock: SymTree)(implicit function: Function, breed: Breed, context: Context, flag: Flag): Instruction = {
        val expr = generateExpr(cond)
        InstructionList(
            expr._1,
            InstructionCompose(f"if(${expr._2})", generate(ifBlock)),
            InstructionCompose(f"else", generate(elseBlock)),
        )
    }

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

    def generateRepeat(number: Expression, content: Instruction)(implicit function: Function, breed: Breed, context: Context):Instruction = {
        val expr = generateExpr(number)
        InstructionList(
            expr._1,
            InstructionCompose(f"(1 to ${expr._2}).map(_ =>", content, ")")
        )
    }

    /**
     * Convert Sym.Tree Expression to Scala String Expression
     *
     * @param	exp: Expression
     * @return	Scala expression as a string
     */
    def generateExpr(expr: Expression)(implicit function: Function, breed: Breed, context: Context): (Instruction, String) = {
        expr match{
            case VariableValue(v) => (EmptyInstruction, f"${v.getGetter()}")
            case BooleanValue(v) => (EmptyInstruction, v.toString())
            case IntValue(v) => (EmptyInstruction, v.toString())
            case FloatValue(v) => (EmptyInstruction, v.toString())
            case StringValue(v) => (EmptyInstruction, "\"" + v.toString() + "\"")
            case BreedValue(breed) => {
                if (breed == context.getObserverBreed()){
                    (EmptyInstruction, Renamer.toValidName(breed.pluralName))
                }
                else{
                    (EmptyInstruction, f"${BreedGen.observerVariable}.${Renamer.toGetterNane(breed.pluralName)}()")
                }
            }
                
            case ListValue(lst) => ???
            case OfValue(expr, from) => {
                val from2 = generateExpr(from)
                val ret = generateOfValue(from2._2, expr)
                (
                    InstructionList(from2._1, ret._1),
                    ret._2
                )
            }

            case IfElseBlockExpression(conds, block) => {
                conds match {
                    case head :: Nil => generateIfElseExpr(head._1, head._2, block)
                    case head :: tail => generateIfElseExpr(head._1, head._2, IfElseBlockExpression(tail, block))
                }
            }

            case BinarayExpr(op, lf, rt) => {
                val left = generateExpr(lf)
                val right = generateExpr(rt)
                (
                    InstructionList(left._1, right._1),
                    f"(${left._2} ${getOperator(op)} ${right._2})"
                )
            }

            case Call(fct, args) => {
                fct match{
                    case fct: LinkedFunction => {
                        val argsPro = args.map(generateExpr(_))
                        val argsCall = argsPro.map(_._2).reduce(_ + ", "+ _)
                        val argsInstr = argsPro.map(_._1)
                        (
                            InstructionList(argsInstr),
                            InstructionGen(f"${fct.name}($argsCall)").generate(0)
                        )
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
        }
    }

    def generateOfValue(set: String, variable: Variable): (Instruction, String) = {
        val vari = getUniqueVariableName()
        val vari2 = getUniqueVariableName()
        val typ = Type.toString(variable.getType())

        (
            InstructionList(
                InstructionGen(f"val $vari = $set.toList.map(a => asyncMessage(() => a.${variable.getGetterName()}))"),
                InstructionCompose(f"while (!($vari.nonEmpty && $vari.forall(x => x.isCompleted)))",
                    InstructionBlock(
                        InstructionGen("waitAndReply(1)")
                    )
                ),
                InstructionGen(f"val $vari2: List[$typ] = $vari.map(o => o.popValue.get).asInstanceOf[List[$typ]]")
            ),
            vari2
        )
    }

    /**
     * Convert NetLogo Operator to Scala Operator
     *
     * @param	op: String	
     * @return	Scala operator
     */
    def getOperator(op: String): String = {
        op match {
            case "=" => "=="
            case _ => op
        }
    }

    
    def getUniqueVariableName():String={
        val id = varCounter
        varCounter += 1
        f"tmp_$id"
    }
}