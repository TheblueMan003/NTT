package analyser

import ast._
import analyser.Types.{BreedType, ListType}
import utils.{Context, ContextMap}
import utils.Reporter

object Splitter{
    var counter = 0
    def analyse(context: Context):Unit = {
        counter = 0
        context.getBreeds().map(b => b.getAllFunctions().map(f => analyse(context, b, f)))
    }

    def analyse(context: Context, breed: Breed, function: Function):Unit = {
        function match{
            case cf: LinkedFunction => {
                if (cf.AST == null){ // Avoid Computing Lambda twice
                    val localVar = ContextMap[Variable]()
                    cf._args.map(v => localVar.add(v.name, v))
                    cf.AST = toAST(cf.body)(context, breed, cf, localVar)
                }
            }
            case bf: BaseFunction => 
        }
    }

    /**
     * Transform a AST into a AST
     */ 
    private def toAST(tree: AST)(implicit context: Context, breed: Breed, function: LinkedFunction, localVar: ContextMap[Variable]): AST = {
        tree match{
            case expr: AST.Expression => toSplittedExpr(expr)
            case AST.Assignment(vari, expr) => 
                AST.Assignment(
                    getVariable(vari.name), 
                    toSplittedExpr(expr)
                )

            case AST.Declaration(variExp, expr) => {
                val vari = function.breed.addVariable(f"${function.name}_${variExp.name}")
                localVar.add(variExp.name, vari)
                AST.Declaration(AST.VariableValue(vari), toSplittedExpr(expr))
            }

            case AST.Block(block) => {
                localVar.push()
                val ret = AST.Block(block.map(toAST(_)))
                localVar.pop()
                ret
            }
            case AST.Report(expr) => {
                AST.Report(toSplittedExpr(expr))
            }
            case AST.IfBlock(cond, block) => AST.IfBlock(toSplittedExpr(cond), toAST(block))
            case AST.IfElseBlock(blocks, elseBlocks) => 
                AST.IfElseBlock(
                    blocks.map(x => (toSplittedExpr(x._1), toAST(x._2))),
                    toAST(elseBlocks)
                )

            case AST.Loop(block) => AST.Loop(toAST(block))
            case AST.Repeat(number, block) => AST.Repeat(toSplittedExpr(number).asInstanceOf[AST.Expression], toAST(block))
            case AST.While(expr, block) => AST.While(toSplittedExpr(expr), toAST(block))
            case AST.Ask(expr, block) => {
                val breeds = getBreedFrom(expr)
                if (breeds.size > 1){
                    Reporter.warning("Ambiguous breed for ask not supported.")
                }

                val b = breeds.head

                val variOwner = new Variable(f"__myself_${localVar.getAskIndex()}") // is myself variable in NetLogo
                variOwner.initConstraints(Set(breed))
                val upperCaller = localVar.getAskVariables()
                localVar.push()
                localVar.add("myself", variOwner)
                localVar.pushAsk(variOwner)

                val lambda = b.addLambda(block, localVar.getAskVariables()) // Create Inner function
                lambda.AST = toAST(block)(context, b, lambda, localVar) // Analyse Inner function


                localVar.popAsk()
                localVar.pop()

                AST.Ask(upperCaller, toSplittedExpr(expr), lambda)
            }
            case AST.CreateBreed(b, nb, block) => {
                localVar.push()

                val lambda = b.name.addLambda(block, localVar.getAskVariables()) // Create Inner function
                lambda.AST = toAST(block)(context, b.name, lambda, localVar) // Analyse Inner function

                localVar.pop()

                AST.CreateBreed(AST.BreedValue(b.name), toSplittedExpr(nb), lambda)
            }

            case _ => tree
        }
    }

    private def toSplittedExpr(tree: AST.Expression)(implicit context: Context, breed: Breed, function: LinkedFunction): (AST, AST.Expression) = {
        tree match{
            case AST.Call(fct, args) => AST.Call(breed.getFunction(fct), args.map(toSplittedExpr(_)))

            case AST.BinarayExpr(op, lf, rt) => AST.BinarayExpr(op, toSplittedExpr(lf), toSplittedExpr(rt))

            case AST.IfElseBlockExpression(blocks, elseBlocks) => 
                AST.IfElseBlockExpression(
                    blocks.map(x => (toSplittedExpr(x._1), toSplittedExpr(x._2))),
                    toSplittedExpr(elseBlocks)
                )

            case AST.ListValue(v) => ???
            case AST.OfValue(v, b) => {
                val name = getVariableName()
                val vari = AST.VariableValue(name)
                (AST.Declaration(vari, tree), vari),
            }
            case AST.WithValue(e, v) => ???
            case _ => (AST.Empty, tree)
        }
    }

    private def getVariableName()={
        val id = counter
        counter += 1
        return "DEFAULT_POLLING_$i"
    }
}