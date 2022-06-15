package analyser

import ast._
import netlogo.Types.{BreedType, ListType}
import netlogo._
import utils.{Context, ContextMap}
import utils.Reporter
import codegen.BreedGen
import codegen.CodeGen

object NameAnalyser{
    def analyse(context: Context):Unit = {
        context.getBreeds().flatMap(b => b.getAllFunctions().map(f => (b, f))).map{ case (b, f) =>{ analyse(context, b, f)}}
    }

    def analyse(context: Context, breed: Breed, function: Function):Unit = {
        function match{
            case cf: LinkedFunction => {
                if (cf.symTree == null){ // Avoid Computing Lambda twice
                    val localVar = new ContextMap[Variable]()
                    cf._args.map(v => localVar.add(v.name, v))
                    cf._args.map(v => {v.name = f"FUNCTION_ARG_${cf.name}_${v.name}";breed.addVariable(v)})
                    breed.addVariable(cf.returnVariable)
                    cf.symTree = toSymTree(cf.body)(context, breed, cf, localVar)
                }
            }
            case bf: BaseFunction => { }
        }
    }

    /**
     * Transform a AST into a SymTree
     */ 
    private def toSymTree(tree: AST)(implicit context: Context, breed: Breed, function: LinkedFunction, localVar: ContextMap[Variable]): SymTree = {
        val newTree = tree match{
            case expr: AST.Expression => toSymTreeExpr(expr)
            case AST.Assignment(vari, expr) => 
                SymTree.Assignment(
                    getVariable(vari.name), 
                    toSymTreeExpr(expr)
                )

            case AST.Declaration(variExp, expr) => {
                val vari = function.breed.addVariable(f"${function.name}_${variExp.name}", true)
                vari.breeds = Set(function.breed)
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

                val variOwner = new Variable(BreedGen.myselfVariableName) // is myself variable in NetLogo
                variOwner.initConstraints(Set(breed))
                
                localVar.push()
                localVar.add("myself", variOwner)
                localVar.pushAsk(variOwner)

                val turtles = toSymTreeExpr(expr)

                val lambda = b.addLambda(block, localVar.getAskVariables(), FunctionType.Ask) // Create Inner function
                lambda.symTree = toSymTree(block)(context, b, lambda, localVar) // Analyse Inner function


                localVar.popAsk()
                localVar.pop()

                SymTree.Ask(variOwner, getBreedFrom(expr).head, turtles, lambda)
            }
            case AST.CreateBreed(b, nb, block) => {
                localVar.push()

                val lambda = b.name.addLambda(block, localVar.getAskVariables(), FunctionType.Create) // Create Inner function
                lambda.symTree = toSymTree(block)(context, b.name, lambda, localVar) // Analyse Inner function

                localVar.pop()

                SymTree.CreateBreed(SymTree.BreedValue(b.name), toSymTreeExpr(nb), lambda)
            }
            case AST.HatchBreed(b, nb, block) => {
                localVar.push()

                val lambda = b.name.addLambda(block, localVar.getAskVariables(), FunctionType.Create) // Create Inner function
                lambda.symTree = toSymTree(block)(context, b.name, lambda, localVar) // Analyse Inner function

                localVar.pop()

                SymTree.HatchBreed(SymTree.BreedValue(b.name), toSymTreeExpr(nb), lambda)
            }
        }
        newTree.setPosition(tree)
        newTree
    }    

    /**
     * Transform AST.Expression into SymTree.Expression
     */ 
    private def toSymTreeExpr(tree: AST.Expression)(implicit context: Context, breed: Breed, function: LinkedFunction, localVar: ContextMap[Variable]): SymTree.Expression = {
        val newTree = tree match{
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
            case AST.OneOfValue(v) => SymTree.OneOfValue(v.map(toSymTreeExpr(_)))
            case AST.Not(v) => SymTree.Not(toSymTreeExpr(v))

            case AST.BreedValue(v) => SymTree.BreedValue(v)
            case AST.OfValue(value, asked) => {
                val breeds = getBreedFrom(asked)
                if (breeds.size > 1){
                    Reporter.warning("Ambiguous breed for ask not supported.")
                }

                val b = breeds.head

                val variOwner = new Variable(BreedGen.myselfVariableName) // is myself variable in NetLogo
                variOwner.initConstraints(Set(b))
                
                localVar.push()
                localVar.add("myself", variOwner)
                localVar.pushAsk(variOwner)

                val lambda = b.addLambda(AST.Report(value), localVar.getAskVariables(), FunctionType.Ask, true) // Create Inner function
                lambda.symTree = toSymTree(AST.Report(value))(context, b, lambda, localVar) // Analyse Inner function
                b.addVariable(lambda.returnVariable)


                localVar.popAsk()
                localVar.pop()

                SymTree.Ask_For_Report(variOwner, b, toSymTreeExpr(asked), lambda)
            }
            case AST.WithValue(e, v) => {
                val b = getBreedFrom(e).head
                val index = b.predicateFilter.size
                b.predicateFilter.addOne(toSymTreeExpr(v)(context, b, function, localVar))

                SymTree.WithValue(toSymTreeExpr(e), b, index)
            }
            case AST.SortBy(value, sorter) => {
                val b = getBreedFrom(value).head
                val index = b.predicateSorter.size
                b.predicateSorter.addOne(toSymTreeExpr(sorter)(context, b, function, localVar))

                SymTree.SortBy(toSymTreeExpr(value), b, index)
            }

            case AST.MinOneAgent(expr) => SymTree.MinOneAgent(toSymTreeExpr(expr))
            case AST.MaxOneAgent(expr) => SymTree.MaxOneAgent(toSymTreeExpr(expr))
            case AST.MinNAgent(expr, nb) => SymTree.MinNAgent(toSymTreeExpr(expr), toSymTreeExpr(nb))
            case AST.MaxNAgent(expr, nb) => SymTree.MaxNAgent(toSymTreeExpr(expr), toSymTreeExpr(nb))
            case AST.BreedAt(b, x, y) => SymTree.BreedAt(toSymTreeExpr(b), toSymTreeExpr(x), toSymTreeExpr(y))
            case AST.BreedAtSingle(b, x, y) => SymTree.BreedAtSingle(toSymTreeExpr(b), toSymTreeExpr(x), toSymTreeExpr(y))
            case AST.BreedOn(b, expr) => SymTree.BreedOn(toSymTreeExpr(b), toSymTreeExpr(expr))
            case AST.Neighbors => SymTree.Neighbors
            case AST.Other(b) => SymTree.Other(toSymTreeExpr(b))
            case AST.Nobody => SymTree.Nobody
            case AST.OneOf(b) => SymTree.OneOf(toSymTreeExpr(b))

            case AST.All(e, v) =>{
                val b = getBreedFrom(e).head
                val index = b.predicateFilter.size
                b.predicateFilter.addOne(toSymTreeExpr(v)(context, b, function, localVar))

                SymTree.All(toSymTreeExpr(e), b, index)
            }
            case AST.Any(e) =>{
                SymTree.Any(toSymTreeExpr(e))
            }
            case AST.Count(b) => SymTree.Count(toSymTreeExpr(b))
        }
        newTree.setPosition(tree)
        newTree
    }

    /**
     * Get the breed from an expression
     */ 
    private def getBreedFrom(expr: AST.Expression)(implicit context: Context, breed: Breed): Set[Breed] = {
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
            case AST.WithValue(expr, predicate) => getBreedFrom(expr)
            case AST.SortBy(expr, sorter) => getBreedFrom(expr)
            case AST.MinNAgent(expr, nb) => getBreedFrom(expr)
            case AST.MaxNAgent(expr, nb) => getBreedFrom(expr)
            case v: AST.VariableValue => {
                if (v.name == "myself"){
                    Set(breed)
                } else {
                    v.removeDuplicatedBreed();v.breeds
                }
            }
            case AST.BreedAt(b, x, y) => getBreedFrom(b)
            case AST.BreedAtSingle(b, x, y) => getBreedFrom(b)
            case AST.BreedOn(b, expr) => getBreedFrom(b)
            case AST.Neighbors => Set(context.getPatchBreed())
            case AST.Other(b) => getBreedFrom(b)
            case AST.Nobody => Set(context.getAgentBreed())
            case AST.OneOf(b) => getBreedFrom(b)
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
            val ret = localVar.getWithOwnerIndex(name)
            val origin = ret._1
            val vari_ = ret._2
            if (origin == -1){
                SymTree.VariableValue(vari_)
            }
            else{
                SymTree.OtherVariable(vari_, origin)
            }
        }
        else if (breed.hasVariable(name)){
            SymTree.VariableValue(breed.getVariable(name))
        }
        else if (context.getObserverBreed().hasVariable(name)){
            SymTree.VariableValue(context.getObserverBreed().getVariable(name))
        }
        else if (name == "myself"){
            SymTree.VariableValue(new Variable(BreedGen.myselfVariableName))
        }
        else{
            throw new Exception(f"Unknown Variable: ${name} in ${breed.pluralName}")
        }
    }
}