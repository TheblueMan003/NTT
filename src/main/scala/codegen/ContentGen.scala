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

object ContentGen{
    var varCounter = 0

    /**
     * Generate Function Content.
     *
     * @param	function
     * @param   breed
     * @return	FunctionGen
     */
    def generate(function: LinkedFunction, breed: Breed)(implicit context: Context): FunctionGen = {
        FunctionGen(Renamer.toValidName(function.name), function._args, Type.toString(function.getType()), generate(function.symTree)(function, breed))
    }

    /**
     * Generate Variables.
     *
     * @param	variables	
     * @return	List[Instruction]
     */
    def generateVariables(variables: Iterable[Variable])(implicit context: Context): List[Instruction] = {
        val exported = variables.filter(_.exported)
        exported.map(generate(_)).toList ::: exported.map(generateGetterSetter(_)).toList.flatten
    }

    /**
     * Generate Getter and Setter for variable.
     *
     * @param	variable: Variable for which to generate the setter
     * @return	List[Instruction] containing setter and getter
     */
    def generateGetterSetter(variable: Variable)(implicit context: Context): List[Instruction] = {
        val typ = Type.toString(variable.getType())
        List(
        InstructionGen(f"def ${variable.getGetterName()}(): ${typ} = ${variable.getName()}"),
        InstructionCompose(f"def ${variable.getSetterName()}(__value : ${typ}): Unit = ", 
            InstructionBlock(List(
                InstructionGen(f"${variable.getName()} = __value"))
            ))
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
    def generate(instr: SymTree)(implicit function: Function, breed: Breed): Instruction = {
        instr match{
            case Block(body) => {
                val content = body.map(generate(_))
                InstructionBlock(content)
            }
            case Declaration(VariableValue(v), value) => {
                val expr = generateExpr(value)
                InstructionList(List(expr._1, InstructionGen(f"${v.name} = ${expr._2}")))
            }
            case Assignment(VariableValue(v), value) => {
                val expr = generateExpr(value)
                InstructionList(List(expr._1, InstructionGen(f"${v.name} = ${expr._2}")))
            }
            case Call(fct, args) => {
                val (prefix, content) = generateExpr(instr.asInstanceOf[SymTree.Expression])
                InstructionList(List(prefix, InstructionGen(content)))
            }
            case CreateBreed(b, nb, fct) => {
                generateRepeat(nb, InstructionBlock(List(
                        InstructionGen(f"${Renamer.toValidName(b.name.pluralName)}.add(new ${b.name.className}(this, 0, 0, ${fct.lambdaIndex}))")
                )))
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
                InstructionList(List(
                    expr._1,
                    InstructionCompose(f"while(${expr._2})", generate(block))
                ))
            }

            case Report(expr) => {
                val expr2 = generateExpr(expr)
                InstructionList(List(
                    expr2._1,
                    InstructionGen(f"${expr2._2}")
                ))
            }

            case Tick => {
                if (function.name != "go"){
                    Reporter.warning("Tick command not supported outside of go method")
                }
                InstructionGen("waitLabel(Turn, 1)")
            }
            case Ask(upperCaller, turtles, block) => {
                val set = generateExpr(turtles)
                val fct = block.name
                val vari = getUniqueVariableName()
                InstructionList(List(
                    set._1,
                    InstructionGen(f"val $vari = ${set._2}.toList.map(s => asyncMessage(() => s.$fct(this)))"),
                    InstructionCompose(f"while(!$vari.forall(_.isCompleted))", InstructionBlock(List(
                        InstructionGen("waitAndReply(1)"))
                    ))
                ))
            }
        }
    }

    def generateRepeat(number: Expression, content: Instruction):Instruction = {
        val expr = generateExpr(number)
        InstructionList(List(
            expr._1,
            InstructionCompose(f"(1 to ${expr._2}).map(_ =>", content, ")")
        ))
    }

    /**
     * Convert Sym.Tree Expression to Scala String Expression
     *
     * @param	exp: Expression
     * @return	Scala expression as a string
     */
    def generateExpr(expr: Expression): (Instruction, String) = {
        expr match{
            case VariableValue(v) => (EmptyInstruction, Renamer.toValidName(v.name))
            case BooleanValue(v) => (EmptyInstruction, v.toString())
            case IntValue(v) => (EmptyInstruction, v.toString())
            case FloatValue(v) => (EmptyInstruction, v.toString())
            case StringValue(v) => (EmptyInstruction, "\"" + v.toString() + "\"")
            case BreedValue(breed) => (EmptyInstruction, Renamer.toValidName(breed.pluralName))
            case ListValue(lst) => ???
            case OfValue(expr, from) => {
                val from2 = generateExpr(from)
                val ret = generateOfValue(from2._2, expr.vari)
                (
                    InstructionList(List(from2._1, ret._1)),
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
                    InstructionList(List(left._1, right._1)),
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
            InstructionList(List(
                InstructionGen(f"val $vari = $set.toList.map(a => asyncMessage(() => a.${variable.getGetterName()}))"),
                InstructionCompose(f"while (!($vari.nonEmpty && $vari.forall(x => x.isCompleted)))",
                    InstructionBlock(List(
                        InstructionGen("waitAndReply(1)"))
                    )
                ),
                InstructionGen(f"val $vari2: List[$typ] = $vari.map(o => o.popValue.get).asInstanceOf[List[$typ]]")
            )),
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