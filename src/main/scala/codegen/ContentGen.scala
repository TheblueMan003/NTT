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
    val logVariableMapName = "DEFAULT_LOG_Variables"

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
            InstructionGen(f"val $logVariableMapName = mutable.Map[String, Any]()"),
            InstructionGen(f"var ${BreedGen.askVaraibleName} = -1")
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
                InstructionGen(f"$logVariableMapName($p${variable.getName()}$p) = DEFAULT_value"),
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
                generateRepeat(nb, InstructionBlock(
                        InstructionGen(f"${Renamer.toValidName(b.name.pluralName)}.add(new ${b.name.className}(this, 0, 0, ${fct.lambdaIndex}))")
                ))
            }

            case IfBlock(cond, block) => InstructionCompose(f"if(${generateExpr(cond)})", generate(block))
            case IfElseBlock(conds, block) => {
                InstructionList(
                    InstructionCompose(f"if(${generateExpr(conds.head._1)})", generate(conds.head._2)) ::
                    conds.drop(1).map(c => 
                        InstructionCompose(f"else if(${generateExpr(c._1)})", generate(c._2))
                    ) ::: List(
                        InstructionCompose("else", generate(block))
                    )
                )
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
                val expr2 = generateExpr(expr)
                InstructionList(
                    expr2._1,
                    InstructionGen(f"${expr2._2}")
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
                        InstructionList(
                            set._1,
                            InstructionGen(f"val $vari = ${set._2}.toList.map(s => WORKER_Turtle(${BreedGen.observerVariable}, this, ${BreedGen.logName}, $fct))"),
                            InstructionGen(f"var $vari3 = false"),
                            InstructionCompose(f"while(!$vari3)", InstructionBlock(
                                InstructionGen(f"val $vari2 = $vari.map(s => asyncMessage(() => s.get_default_is_done()))"),
                                InstructionCompose(f"while(!$vari.forall(_.isCompleted))", InstructionBlock(
                                    InstructionGen("waitAndReply(1)")
                                )),
                                InstructionGen(f"$vari3 = $vari2.map(o => o.popValue.get).asInstanceOf[List[Boolean]].all(_)")
                            ))
                        )
                    }
                }
            }
        }
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
                (
                    EmptyInstruction,
                    (conds.map(c => f"if (${generateExpr(c._1)})${generateExpr(c._2)}")
                        ::: List(generateExpr(block)))
                        .foldLeft("")((x,y) => f"${x} else ${y}")
                )
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