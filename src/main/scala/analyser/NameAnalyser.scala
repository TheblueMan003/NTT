package analyser

import ast._
import netlogo.Types.{BreedType, ListType}
import netlogo._
import utils.{Context, ContextMap}
import utils.Reporter

object NameAnalyser{
    def analyse(context: Context):Unit = {
        context.getBreeds().map(b => b.getAllFunctions().map(f => analyse(context, b, f)))
    }

    def analyse(context: Context, breed: Breed, function: Function):Unit = {
        function match{
            case cf: LinkedFunction => {
                if (cf.symTree == null){ // Avoid Computing Lambda twice
                    val localVar = ContextMap[Variable]()
                    cf._args.map(v => localVar.add(v.name, v))
                    cf.symTree = toSymTree(cf.body)(context, breed, cf, localVar)
                }
            }
            case bf: BaseFunction => 
        }
    }

    /**
     * Transform a AST into a SymTree
     */ 
    private def toSymTree(tree: AST)(implicit context: Context, breed: Breed, function: LinkedFunction, localVar: ContextMap[Variable]): SymTree = {
        tree match{
            case expr: AST.Expression => toSymTreeExpr(expr)
            case AST.Assignment(vari, expr) => 
                SymTree.Assignment(
                    getVariable(vari.name), 
                    toSymTreeExpr(expr)
                )

            case AST.Declaration(variExp, expr) => {
                val vari = function.breed.addVariable(f"${function.name}_${variExp.name}")
                localVar.add(variExp.name, vari)
                SymTree.Declaration(SymTree.VariableValue(vari), toSymTreeExpr(expr))
            }

            case AST.Block(block) => {
                localVar.push()
                val ret = SymTree.Block(block.map(toSymTree(_)))
                localVar.pop()
                ret
            }
            case AST.Report(expr) => {
                SymTree.Report(toSymTreeExpr(expr))
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

                val lambda = b.addLambda(block, localVar.getAskVariables(), FunctionType.Ask) // Create Inner function
                lambda.symTree = toSymTree(block)(context, b, lambda, localVar) // Analyse Inner function


                localVar.popAsk()
                localVar.pop()

                SymTree.Ask(upperCaller, toSymTreeExpr(expr), lambda)
            }
            case AST.CreateBreed(b, nb, block) => {
                localVar.push()

                val lambda = b.name.addLambda(block, localVar.getAskVariables(), FunctionType.Create) // Create Inner function
                lambda.symTree = toSymTree(block)(context, b.name, lambda, localVar) // Analyse Inner function

                localVar.pop()

                SymTree.CreateBreed(SymTree.BreedValue(b.name), toSymTreeExpr(nb), lambda)
            }

            case AST.Tick => SymTree.Tick
        }
    }

    /**
     * Get the breed from an expression
     */ 
    private def getBreedFrom(expr: AST.Expression)(implicit context: Context): Set[Breed] = {
        expr match{
            case AST.BreedValue(b) => Set(b)
            case AST.Call(name, args) => {
                context.getFunction(name) match{
                    case cf: UnlinkedFunction => cf.returnValue.breeds
                    case bf: BaseFunction => {
                        bf.returnType match{
                            case BreedType(t) => Set(t)
                            case ListType(BreedType(t)) => Set(t)
                            case _ => throw new Exception(f"Function ${bf._name} does not return a breeds.")
                        }
                    }  
                }
            }
            case v: AST.VariableValue => v.removeDuplicatedBreed();v.breeds
        }
    }



    /**
     * Transform AST.Expression into SymTree.Expression
     */ 
    private def toSymTreeExpr(tree: AST.Expression)(implicit context: Context, breed: Breed, function: LinkedFunction, localVar: ContextMap[Variable]): SymTree.Expression = {
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
            case AST.BreedValue(v) => SymTree.BreedValue(v)
            case AST.OfValue(v, b) => SymTree.OfValue(toSymTreeExpr(v)(context, getBreedFrom(b).head, function, localVar).asInstanceOf[SymTree.VariableValue], toSymTreeExpr(b))
            case AST.WithValue(e, v) => SymTree.WithValue(toSymTreeExpr(e), toSymTreeExpr(v)(context, getBreedFrom(e).head, function, localVar))
        }
    }

    /**
     * Return a variable from a breed. (Used for [value of turtles])
     */ 
    private def getVariableStrict(name: String)(implicit context: Context, breed: Breed, localVar: ContextMap[Variable]): SymTree.VariableValue = {
        SymTree.VariableValue(breed.getVariable(name))
    }

    /**
     * If the variable is available in the current breed return a SymTree.VariableValue
     * Otherwise return a SymTree.OfValue (Getter for external variable)
     */ 
    private def getVariable(name: String)(implicit context: Context, breed: Breed, localVar: ContextMap[Variable]): SymTree.VariableLike = {
        if (localVar.contains(name)){
            val ret = localVar.getWithOwner(name)
            val origin = ret._1
            val vari_ = ret._2
            if (origin == null){
                SymTree.VariableValue(vari_)
            }
            else{
                SymTree.OfValue(SymTree.VariableValue(origin), SymTree.VariableValue(vari_))
            }
        }
        else if (breed.hasVariable(name)){
            SymTree.VariableValue(breed.getVariable(name))
        }
        else if (context.getObserverBreed().hasVariable(name)){
            SymTree.VariableValue(context.getObserverBreed().getVariable(name))
        }
        else{
            throw new Exception(f"Unknown Variable: ${name} in ${breed.pluralName}")
        }
    }
}