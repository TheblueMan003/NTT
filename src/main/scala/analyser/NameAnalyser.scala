package analyser

import ast._
import utils.{Context, ContextMap}
import utils.Reporter

object NameAnalyser{
    def analyse(context: Context) = {
    }

    private def toSymTree(tree: AST)(implicit context: Context, breed: Breed, function: LinkedASTFunction, localVar: ContextMap[Variable]): SymTree = {
        tree match{
            case expr: AST.Expression => toSymTreeExpr(expr)
            case AST.Assignment(vari, expr) => 
                SymTree.Assignment(
                    getVariable(vari.name), 
                    toSymTreeExpr(expr)
                )

            case AST.Declaration(variExp, expr) => {
                val vari = function.breed.addVariable(f"${function.name}_${variExp.name}")
                SymTree.Declaration(SymTree.VariableValue(vari), toSymTreeExpr(expr))
            }

            case AST.Block(block) => {
                localVar.push()
                val ret = SymTree.Block(block.map(toSymTree(_)))
                localVar.pop()
                ret
            }
            case AST.IfBlock(cond, block) => SymTree.IfBlock(toSymTreeExpr(cond), toSymTree(block))
            case AST.IfElseBlock(blocks, elseBlocks) => 
                SymTree.IfElseBlock(
                    blocks.map(x => (toSymTreeExpr(x._1), toSymTree(x._2))),
                    toSymTree(elseBlocks)
                )

            case AST.Loop(block) => SymTree.Loop(toSymTree(block))
            case AST.Repeat(number, block) => SymTree.Repeat(toSymTreeExpr(number).asInstanceOf[SymTree.Expression], toSymTree(block))
            case AST.While(expr, block) => SymTree.While(toSymTreeExpr(expr), toSymTree(block))
            case AST.Ask(expr, block) => ???
        }
    }

    private def toSymTreeExpr(tree: AST.Expression)(implicit context: Context, breed: Breed, function: LinkedASTFunction, localVar: ContextMap[Variable]): SymTree.Expression = {
        tree match{
            case AST.Call(fct, args) => SymTree.Call(breed.getFunction(fct), args.map(toSymTreeExpr(_)))

            case AST.BinarayExpr(op, lf, rt) => SymTree.BinarayExpr(op, toSymTreeExpr(lf), toSymTreeExpr(rt))

            case AST.IfElseBlockExpression(blocks, elseBlocks) => 
                SymTree.IfElseBlockExpression(
                    blocks.map(x => (toSymTreeExpr(x._1), toSymTreeExpr(x._2))),
                    toSymTreeExpr(elseBlocks)
                )

            case AST.BooleanValue(v) => SymTree.BooleanValue(v)
            case AST.IntValue(v) => SymTree.IntValue(v)
            case AST.FloatValue(v) => SymTree.FloatValue(v)
            case AST.StringValue(v) => SymTree.StringValue(v)
            case AST.ListValue(v) => SymTree.ListValue(v.map(toSymTreeExpr(_)))
            case AST.VariableValue(v) => getVariable(v)
            case AST.OfValue(e, v) => SymTree.OfValue(toSymTreeExpr(e), getVariable(v))
        }
    }

    private def getVariable(name: String)(implicit context: Context, breed: Breed, function: LinkedASTFunction, localVar: ContextMap[Variable]): SymTree.VariableValue = {
        if (localVar.contains(name)){
            SymTree.VariableValue(localVar.get(name))
        }
        else if (breed.hasVariable(name)){
            SymTree.VariableValue(breed.getVariable(name))
        }
        else if (context.getObserverBreed().hasVariable(name)){
            SymTree.VariableValue(context.getObserverBreed().getVariable(name))
        }
        else{
            throw new Exception(f"Unknown Variable: ${name}")
        }
    }
}