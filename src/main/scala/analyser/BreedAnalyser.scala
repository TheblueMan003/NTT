package analyser

import utils.Context
import utils.ContextMap
import ast.CompiledFunction
import ast.AST._
import analyser.BreedConstrainer._
import ast._

object BreedAnalyser{

    /**
    *  Analyse breeds contraint and force function to belong to a breed.
    */ 
    def analyse(context: Context) = {
        initConstraints(context)

        val constraints = generateConstraints(context)

        resolveConstraits(constraints)

        removeDuplicatedBreed(context)
    }

    /**
     * Set All Function to belong to all breed
     */ 
    private def initConstraints(context: Context) = {
        context.functions.values.map(
            _ match {
                case cf: CompiledFunction => cf.initConstraints(context.getBreeds()) 
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
                case cf: CompiledFunction => {
                    val vars = ContextMap[VariableValue]()
                    cf.argsNames.map(x => vars.add(x, VariableValue(x)))
                    analyse(cf.body)(context, BreedOwn(cf), vars)
                }
                case _ => List()
            }
        ).reduce(_ ::: _)
    }


    /** 
     * Get All Breed Constraint from a function
     */ 
    private def analyse(tree: AST)(implicit context: Context, found: BreedConstrainer, localVar: ContextMap[VariableValue]): (List[BreedConstraint]) = {
        tree match{
            case BooleanValue(_) => Nil
            case IntValue(_) => Nil
            case FloatValue(_) => Nil
            case StringValue(_) => Nil

            case Call(name, args) => List(BreedConstraint(found, BreedOwn(context.getFunction(name))))
            case VariableValue(name) => {
                if (localVar.contains(name)){
                    Nil
                }
                else{
                    List(BreedConstraint(found, BreedSet(context.getBreedsWithVariable(name))))
                }
            }

            case Declaration(vari, expr) => {
                localVar.add(vari.name, vari)
                analyse(vari) ::: analyse(expr)
            }
            case Assignment(vari, expr) => {
                analyse(vari) ::: analyse(expr)
            }
            
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
                localVar.push()
                val ret = if (content.isEmpty){
                    Nil
                } else {
                    content.map(analyse(_)).reduce(_ ::: _)
                }
                localVar.pop()
                ret
            }
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
                                    throw new BreedException()
                                }
                            }
                            case BreedOwn(owner) => {
                                if (owner.canBeRestrainTo(expSet)){
                                    changed |= owner.restrainTo(expSet)
                                }
                                else{
                                    throw new BreedException()
                                }
                            }
                        }
                    }
                    // Expect Breed Owner
                    case BreedOwn(ownerExp) => {
                        it.found match{
                            case BreedSet(foundSet) => ???
                            case BreedOwn(owner) => {
                                if (owner.canBeRestrainTo(ownerExp)){
                                    changed |= owner.restrainTo(ownerExp)
                                }
                                else{
                                    throw new BreedException()
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
        context.functions.values.map(removeDuplicatedBreed(_))
    }

    /**
     * Force the function to belong to the highest breed(s) in the breed AST.
     * For Instance if the function belong to Turtle and something that inherite turtle. It will only belong to turtle
     */ 
    private def removeDuplicatedBreed(function: BreedOwned):Unit = {
        function.breeds = function.breeds.filter(breed => !function.breeds.contains(breed.parent))
    }
}