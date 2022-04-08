package codegen

import analyser.SymTree._
import analyser.SymTree
import utils.Context

object CodeGen{
    def generate(context: Context) = {

    }
    def generate(instr: SymTree): String = {
        instr match{
            case Block(body) => {
                val content = body.map(generate(_)).foldLeft("")(_ +"\n"+ _)
                f"{$content}"
            }
            case IfBlock(cond, block) => f"if(${generateExpr(cond)})${generate(block)}"
            case IfElseBlock(conds, block) => {
                (conds.map(c => f"if (${generateExpr(c._1)})${generate(c._2)}")
                     ::: List(generate(block)))
                     .foldLeft("")((x,y) => f"${x} else ${y}")
            }

            case Loop(block) => f"while(true)${generate(block)}"
            case Repeat(number, block) => ???
            case While(cond, block) => f"while(${generateExpr(cond)})${generate(block)}"
        }
    }
    def generateExpr(expr: Expression): String = {
        expr match{
            case vl: VariableLike => ???
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

            case BinarayExpr(op, lf, rt) => f"(${generateExpr(lf)}) $op ${generateExpr(rt)})"

            case Call(fct, args) => ???
        }
    }
}