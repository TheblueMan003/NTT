package analyser

import utils.Context
import utils.ContextMap
import ast.AST._
import analyser.BreedConstrainer._
import ast._
import netlogo.Types.{BreedType, ListType}
import netlogo._

object BreedAnalyser{
    private val observersFunctions = Set("go", "setup")
    private val emptyConst = List[BreedConstraint]()


    /**
    *  generateConstraints breeds contraint and force function to belong to a breed.
    */ 
    def analyse(context: Context) = {
        initConstraints(context)

        val constraints = generateConstraints(context)

        resolveConstraits(constraints)

        removeDuplicatedBreed(context)

        assignBreedToFunction(context)
    }

    /**
     * Set All Function to belong to all breed
     */ 
    private def initConstraints(context: Context) = {
        context.functions.values.map(
            _ match {
                case cf: UnlinkedFunction => {
                    if (observersFunctions.contains(cf.name)){
                        cf.initConstraints(Set(context.getObserverBreed()))
                    }
                    else{
                        cf.initConstraints(context.getBreeds())
                    }
                }
                case _ =>
            }
        )
    }

    /**
     * Get All Breed Constraint from all the code
     */ 
    private def generateConstraints(context: Context): List[BreedConstraint] = {
        context.functions.values.map(
            _ match {
                case cf: UnlinkedFunction => {
                    val vars = new ContextMap[VariableValue]()
                    cf.argsNames.map(x => vars.add(x, VariableValue(x)))
                    generateConstraints(cf.body)(context, BreedOwn(cf), vars)
                }
                case _ => List()
            }
        ).foldLeft(emptyConst)(_ ::: _)
    }

    /** 
     * Get All Breed Constraint from a function
     */ 
    private def generateConstraints(tree: AST)(implicit context: Context, found: BreedConstrainer, localVar: ContextMap[VariableValue]): (List[BreedConstraint]) = {
        tree match{
            case Call("tick", List()) => List(BreedConstraint(found, BreedSet(Set(context.getObserverBreed()))))
            case BooleanValue(_) => Nil
            case IntValue(_) => Nil
            case FloatValue(_) => Nil
            case StringValue(_) => Nil
            case OneOfValue(values) => values.map(generateConstraints(_)).flatten
            case Not(value) => generateConstraints(value)


            case BreedValue(_) => Nil
            case WithValue(value, predicate) => {
                generateConstraints(predicate)(context, getBreedFrom(value), localVar)
            }
            case OfValue(expr, from) => {
                generateConstraints(expr)(context, getBreedFrom(from), localVar)
            }
            case SortBy(expr, sorter) => {
                generateConstraints(sorter)(context, getBreedFrom(expr), localVar)
            }
            case MinOneAgent(expr) => generateConstraints(expr)
            case MaxOneAgent(expr) => generateConstraints(expr)
            case MinNAgent(expr, nb) => generateConstraints(expr):::generateConstraints(nb)
            case MaxNAgent(expr, nb) => generateConstraints(expr):::generateConstraints(nb)
            case BreedAt(breed, x, y) => {
                generateConstraints(x):::
                generateConstraints(y):::
                generateConstraints(breed)
            }
            case BreedAtSingle(breed, x, y) => {
                generateConstraints(x):::
                generateConstraints(y):::
                generateConstraints(breed)
            }
            case BreedOn(breed, set) => {
                generateConstraints(set):::
                generateConstraints(breed)
            }
            case Neighbors => {
                Nil
            }
            case Nobody => {
                Nil
            }
            case Other(breed) => {
                generateConstraints(breed)
            }
            case OneOf(breed) => {
                generateConstraints(breed)
            }


            case All(breed, predicate) => {
                generateConstraints(breed):::
                generateConstraints(predicate)(context, getBreedFrom(breed), localVar)
            }
            case Any(breed) => {
                generateConstraints(breed)
            }
            case Count(breed) => {
                generateConstraints(breed)
            }


            case Call(name, args) => 
                List(BreedConstraint(found, getFunctionBreeds(name))) ::: args.map(generateConstraints(_)).foldLeft(emptyConst)(_ ::: _)
            case VariableValue(name) => {
                if (localVar.contains(name)){
                    Nil
                }
                else if (context.getObserverBreed().hasVariable(name)){
                    Nil
                }
                else{
                    val breeds = context.getBreedsWithVariable(name)
                    if (breeds.isEmpty){
                        if (name == "myself")
                        {
                            Nil
                        }
                        else{
                            throw new Exception(f"Unknown variable: $name at ${tree.positionString()}")
                        }
                    }
                    else{
                        List(BreedConstraint(found, BreedSet(context.getBreedsWithVariable(name))))
                    }
                }
            }

            

            case Declaration(vari, expr) => {
                vari.initConstraints(context.getBreeds())
                localVar.add(vari.name, vari)
                generateConstraints(vari) ::: generateConstraints(expr)
            }
            case Assignment(vari, expr) => {
                generateConstraints(vari) ::: generateConstraints(expr)
            }
            case Report(expr) => {
                generateConstraints(expr)
            }
            
            case BinarayExpr(op, lf, rt) => {
                generateConstraints(lf) ::: generateConstraints(rt)
            }
            case IfElseBlockExpression(ifs, elze) => {
                ifs.map(b => generateConstraints(b._1):::generateConstraints(b._2)).foldLeft(emptyConst)(_ ::: _) ::: generateConstraints(elze)
            }
            case IfElseBlock(ifs, elze) => {
                ifs.map(b => generateConstraints(b._1):::generateConstraints(b._2)).foldLeft(emptyConst)(_ ::: _) ::: generateConstraints(elze)
            }
            case IfBlock(expr, block) => {
                generateConstraints(expr) ::: generateConstraints(block)
            }
            case Repeat(expr, block) => {
                generateConstraints(expr) ::: generateConstraints(block)
            }
            case While(expr, block) => {
                generateConstraints(expr) ::: generateConstraints(block)
            }
            case Ask(expr, block) => {
                localVar.push()

                val vari = VariableValue("myself")
                vari.initConstraints(context.getBreeds())
                localVar.add("myself", vari)

                val ret = List(BreedConstraint(found, BreedOwn(vari))) :::
                          generateConstraints(block)(context, getBreedFrom(expr), localVar)

                localVar.pop()

                ret
            }
            case CreateBreed(breed, nb, block) => {
                localVar.push()

                val ret = List(BreedConstraint(found, BreedSet(Set(context.getObserverBreed())))) :::
                          generateConstraints(block)(context, getBreedFrom(breed), localVar)

                localVar.pop()

                ret
            }
            case HatchBreed(breed, nb, block) => {
                localVar.push()

                val ret = List(BreedConstraint(found, BreedSet(context.getBreeds().diff(Set(context.getObserverBreed()))))) :::
                          generateConstraints(block)(context, getBreedFrom(breed), localVar)

                localVar.pop()

                ret
            }
            case Block(content) => {
                localVar.push()
                val ret = if (content.isEmpty){
                    Nil
                } else {
                    content.map(generateConstraints(_)).foldLeft(emptyConst)(_ ::: _)
                }
                localVar.pop()
                ret
            }
        }
    }

    /**
     * Return BreedConstrainer from a function
     */ 
    private def getFunctionBreeds(name: String)(implicit context: Context):BreedConstrainer = {
        val baseFuncsBreed = context.getBreedsWithFunction(name)
        if (baseFuncsBreed.isEmpty){
            BreedOwn(context.getFunction(name))
        }
        else{
            BreedSet(baseFuncsBreed)
        }
    }

    /**
     * Return BreedConstrainer from an expression value
     */ 
    private def getBreedFrom(expr: Expression)(implicit context: Context): BreedConstrainer = {
        expr match{
            case BreedValue(b) => BreedSet(Set(b))
            case Call(name, args) => {
                context.getFunction(name) match{
                    case cf: UnlinkedFunction => BreedOwn(cf.returnValue)
                    case bf: BaseFunction => {
                        bf.returnType match{
                            case BreedType(t) => BreedSet(Set(t))
                            case ListType(BreedType(t)) => BreedSet(Set(t))
                            case _ => throw new Exception(f"Function ${bf._name} does not return a breeds.")
                        }
                    }  
                }
            }
            case WithValue(value, predicate) => {
                getBreedFrom(value)
            }
            case v: VariableValue => {
                v.initConstraints(context.getBreeds())
                BreedOwn(v)
            }
            case AST.MinNAgent(expr, nb) => getBreedFrom(expr)
            case AST.MaxNAgent(expr, nb) => getBreedFrom(expr)
            case AST.SortBy(expr, sorter) => getBreedFrom(expr)
            case AST.BreedAt(breed, x, y) => getBreedFrom(breed)
            case AST.BreedAtSingle(breed, x, y) => getBreedFrom(breed)
            case AST.BreedOn(breed, expr) => getBreedFrom(breed)
            case AST.Neighbors => BreedSet(Set(context.getPatchBreed()))
            case AST.Other(breed) => getBreedFrom(breed)
            case AST.Nobody => BreedSet(Set(context.getAgentBreed()))
            case AST.OneOf(breed) => getBreedFrom(breed)
        }
    }


    /**
     * Check that All Breed Constraint matches and restrain function to breeds
     */ 
    private def resolveConstraits(constraints: List[BreedConstraint]) = {
        var changed = true
        while(changed){
            changed = false
            constraints.map(it =>
                it.expected match{
                    // Expect Breed Set
                    case BreedSet(expSet) => {
                        it.found match{
                            case BreedSet(foundSet) => {
                                if (!(foundSet subsetOf expSet)){
                                    throw BreedException(it.found, it.expected)
                                }
                            }
                            case BreedOwn(owner) => {
                                if (owner.canBreedBeRestrainTo(expSet)){
                                    changed |= owner.restrainBreedTo(expSet)
                                }
                                else{
                                    throw BreedException(it.found, it.expected)
                                }
                            }
                        }
                    }
                    // Expect Breed Owner
                    case BreedOwn(ownerExp) => {
                        it.found match{
                            case BreedSet(foundSet) => {
                                if (ownerExp.canBreedBeRestrainTo(foundSet)){
                                    changed |= ownerExp.restrainBreedTo(foundSet)
                                }
                                else{
                                    throw BreedException(it.found, it.expected)
                                }
                            }
                            case BreedOwn(owner) => {
                                if (owner.canBreedBeRestrainTo(ownerExp)){
                                    changed |= owner.restrainBreedTo(ownerExp)
                                }
                                else{
                                    throw BreedException(it.found, it.expected)
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    /**
     * Force all function to belong to the highest breed(s) in the breed AST.
     */
    private def removeDuplicatedBreed(context: Context):Unit = {
        context.functions.values.map(_.removeDuplicatedBreed())
        context.getBreeds().flatMap(_.getAllVariables()).map(_.removeDuplicatedBreed())
    }

    /**
     * Put the function inside the breeds it belong. Duplicate the function if belong to more than one breed.
     */ 
    private def assignBreedToFunction(context: Context):Unit = {
        context.functions.values.map(f =>
            f match {
                case bf: BaseFunction =>
                case cf: UnlinkedFunction => cf.breeds.map( breed => 
                    breed.addFunction(
                        LinkedFunction(cf._name, cf.argsNames.map(new Variable(_)), cf.body, breed, cf.hasReturnValue)
                    )
                )
            }
        )
    }
}