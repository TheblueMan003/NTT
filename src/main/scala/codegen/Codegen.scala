package codegen

import analyser.SymTree._
import analyser.SymTree
import utils.Context
import ast.Breed
import ast.{LinkedFunction, Function}
import ast.Variable
import analyser.Types._
import analyser.Type
import utils.Reporter

object CodeGen{
    def generate(context: Context): Set[ClassFile] = {
        context.getBreeds().map(generate(_))
    }
    private def generate(breed: Breed): ClassFile = {
        ClassFile(
            breed.singularName,
            generateVariables(breed.getAllVariables()), 
            generateFunctions(breed.getAllFunctions(), breed),
            List(),
            List()
        )
    }
    private def generateFunctions(function: Iterable[Function], breed: Breed): List[FunctionGen] = {
        function.map{
            _ match {
                case lc: LinkedFunction => generate(lc, breed)
                case _ => null
            }
        }.filter(_ != null).toList
    }
    private def generate(function: LinkedFunction, breed: Breed): FunctionGen = {
        FunctionGen(function.name, Type.toString(function.getType()), generate(function.symTree)(function, breed))
    }
    private def generateVariables(variables: Iterable[Variable]): List[InstructionGen] = {
        variables.map(generate(_)).toList
    }
    private def generate(variable: Variable): InstructionGen = {
        val typ = variable.getType()
        InstructionGen(f"var ${variable.name} : ${Type.toString(typ)} = ${Type.defaultValue(typ)}")
    }
    private def generate(instr: SymTree)(implicit function: Function, breed: Breed): Instruction = {
        instr match{
            case Block(body) => {
                val content = body.map(generate(_))
                InstructionBlock(content)
            }
            case Declaration(VariableValue(v), value) => InstructionGen(f"${v.name} = ${generateExpr(value)}")
            case Assignment(VariableValue(v), value) => InstructionGen(f"${v.name} = ${generateExpr(value)}")
            case Call(fct, args) => {
                val argsInstr = args.map(generateExpr(_)).reduce(_ + ", "+ _)
                InstructionGen(f"${fct.name}($argsInstr)")
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
            case Repeat(number, block) => ???
            case While(cond, block) => InstructionCompose(f"while(${generateExpr(cond)})", generate(block))

            case Tick => {
                if (function.name != "go"){
                 Reporter.warning("Tick command not supported outside of go method")
                }
                InstructionGen("waitLabel(Turn, 1)")
            }
        }
    }
    private def generateExpr(expr: Expression): String = {
        expr match{
            case VariableValue(v) => v.name
            case BooleanValue(v) => v.toString()
            case IntValue(v) => v.toString()
            case FloatValue(v) => v.toString()
            case StringValue(v) => "\"" + v.toString() + "\""
            case BreedValue(breed) => ???
            case ListValue(lst) => ???

            case IfElseBlockExpression(conds, block) => {
                (conds.map(c => f"if (${generateExpr(c._1)})${generateExpr(c._2)}")
                     ::: List(generateExpr(block)))
                     .foldLeft("")((x,y) => f"${x} else ${y}")
            }

            case BinarayExpr(op, lf, rt) => f"(${generateExpr(lf)} ${getOperator(op)} ${generateExpr(rt)})"

            case Call(fct, args) => {
                val argsInstr = args.map(generateExpr(_)).reduce(_ + ", "+ _)
                InstructionGen(f"${fct.name}($argsInstr)").generate(0)
            }
        }
    }

    private def getOperator(op: String): String = {
        op match {
            case "=" => "=="
            case _ => op
        }
    }
}