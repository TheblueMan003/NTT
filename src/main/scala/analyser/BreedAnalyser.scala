package analyser

import utils.Context
import ast.CompiledFunction
import ast.Tree._
import analyser.BreedConstrainer._
import ast._

object BreedAnalyser{
    def analyse(context: Context) = {
        initConstraints()(context)
    }
    def initConstraints()(implicit context: Context) = {
        context.functions.values.map(
            _ match {
                case cf: CompiledFunction => cf.initConstraints(context.getBreeds()) 
                case _ =>
            }
        )
    }
    def analyse(tree: Tree)(implicit context: Context, found: BreedConstrainer): (List[BreedConstraint]) = {
        tree match{
            case BooleanValue(_) => Nil
            case IntValue(_) => Nil
            case FloatValue(_) => Nil
            case StringValue(_) => Nil

            case Call(name, args) => List(BreedConstraint(found, BreedOwn(context.getFunction(name))))
            case VariableValue(name) => List(BreedConstraint(found, BreedSet(context.getBreedsWithVariable(name))))
            
            case BinarayExpr(op, lf, rt) => {
                analyse(lf) ::: analyse(rt)
            }
            case IfElseBlockExpression(ifs, elze) => {
                ifs.map(b => analyse(b._1):::analyse(b._2)).reduce(_ ::: _) ::: analyse(elze)
            }
            case IfElseBlock(ifs, elze) => {
                ifs.map(b => analyse(b._1):::analyse(b._2)).reduce(_ ::: _) ::: analyse(elze)
            }
            case IfBlock(expr, block) => {
                analyse(expr) ::: analyse(block)
            }
            case Repeat(expr, block) => {
                analyse(expr) ::: analyse(block)
            }
            case While(expr, block) => {
                analyse(expr) ::: analyse(block)
            }
            case Ask(block) => {
                analyse(block)
            }
            case Block(content) => {
                content.map(analyse(_)).reduce(_ ::: _)
            }
        }
    }

    def resolveConstraits(constraints: List[BreedConstrainer]){
        var changed = true
        while(changed){
            constraints.map(it =>
                


            )
        }
    }
}