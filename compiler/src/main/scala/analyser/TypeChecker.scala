package analyser

import SymTree._
import TypeConstrainer._
import utils.Context
import netlogo.{Variable, LinkedFunction, Function, Types, Breed, BaseFunction}
import netlogo.Types._
import netlogo.Type
import ast.AST

object TypeChecker{
    def analyse(context: Context) = {
        val constraints = genConstraints(context)
        
        resolveConstraits(constraints)
    }
    def genConstraints(context: Context): List[TypeConstraint] = {
        context.getBreeds().flatMap(breed => breed.getAllFunctions().flatMap(
            _ match{
                case lf: LinkedFunction => genConstraints(lf.symTree)(context, lf, breed)
                case _ => Nil
            }
        )).toList
    }
    def genConstraints(tree: SymTree)(implicit context: Context, function: Function, currentBreed: Breed): List[TypeConstraint] = {
        tree match{
            case Call(fct, args) => {
                fct.getArguments().zip(args).flatMap{case (v, e) =>
                    genConstraintsExpr(e)(context, TypeOwn(v), currentBreed)
                }
            }
            case CreateBreed(breed, nb, block) => genConstraintsExpr(nb)(context,DirectType(IntType), currentBreed)
            case HatchBreed(breed, nb, block) => genConstraintsExpr(nb)(context,DirectType(IntType), currentBreed)
            case Assignment(vari, value) => genConstraintsExpr(value)(context, getVariableConstraint(vari), currentBreed)
            case Declaration(vari, value) => genConstraintsExpr(value)(context, getVariableConstraint(vari), currentBreed)
            case Block(block) => block.map(genConstraints(_)).foldLeft(List[TypeConstraint]())(_ ::: _)
            case Report(expr) => genConstraintsExpr(expr)(context, TypeOwn(function.returnVariable), currentBreed)

            case IfBlock(cond, block) => {
                genConstraintsExpr(cond)(context, DirectType(BoolType), currentBreed) ::: genConstraints(block)
            }
            case IfElseBlock(conds, block) => {
                conds.flatMap(c => 
                    genConstraintsExpr(c._1)(context, DirectType(BoolType), currentBreed) :::
                    genConstraints(c._2)
                ) ::: genConstraints(block)
            }
            case Loop(block) => genConstraints(block)
            case Repeat(expr, block) => genConstraintsExpr(expr)(context, DirectType(IntType), currentBreed) ::: genConstraints(block)
            case While(expr, block) => genConstraintsExpr(expr)(context, DirectType(BoolType), currentBreed) ::: genConstraints(block)
            case Ask(variOwner, _, turtles, block) => {
                val varT = new TypedVariable()
                genConstraintsExpr(turtles)(context, TypeOwn(varT), currentBreed) ::: 
                List(TypeConstraint(DirectType(BreedType(currentBreed)), getVariableConstraint(VariableValue(variOwner)), tree))
            }
            case expr: Expression => throw new Exception(f"Lonely expression: ${expr}")
        }
    }
    def genConstraintsExpr(expr: Expression)(implicit context: Context, found: TypeConstrainer, currentBreed: Breed): List[TypeConstraint] = {
        List(TypeConstraint(found, TypeOwn(expr), expr))::: (
        expr match{
            case vl: VariableLike => List(TypeConstraint(found, getVariableConstraint(vl), expr))
            case BooleanValue(_) => List(TypeConstraint(found, DirectType(BoolType), expr))
            case IntValue(_) => List(TypeConstraint(found, DirectType(IntType), expr))
            case FloatValue(_) => List(TypeConstraint(found, DirectType(FloatType), expr))
            case StringValue(_) => List(TypeConstraint(found, DirectType(StringType), expr))
            case BreedValue(breed) => List(TypeConstraint(found, DirectType(BreedSetType(breed)), expr))
            case OneOfValue(values) => {
                val varT = new TypedVariable()
                TypeConstraint(found, TypeOwn(varT), expr) :: values.map(genConstraintsExpr(_)(context, TypeOwn(varT), currentBreed)).flatten
            }
            case Not(value) => TypeConstraint(found, DirectType(BoolType), expr) :: genConstraintsExpr(value)(context, DirectType(BoolType), currentBreed)

            // Set Operation
            case WithValue(value, b, i) => genConstraintsExpr(b.predicateFilter(i))(context, DirectType(BoolType), b)
            case SortBy(value, b, i) => genConstraintsExpr(b.predicateSorter(i))(context, TypeOwn(new TypedVariable()), b)
            case MinOneAgent(expr) => genConstraintsExpr(expr):::List(TypeConstraint(found, DirectType(BreedType(getBreedFrom(expr).head)), expr))
            case MaxOneAgent(expr) => genConstraintsExpr(expr):::List(TypeConstraint(found, DirectType(BreedType(getBreedFrom(expr).head)), expr))
            case MinNAgent(expr, nb) => {
                genConstraintsExpr(expr):::
                    genConstraintsExpr(nb)(context, DirectType(IntType), currentBreed):::
                    List(TypeConstraint(found, DirectType(BreedSetType(getBreedFrom(expr).head)), expr))
            }
            case MaxNAgent(expr, nb) => {
                genConstraintsExpr(expr):::
                    genConstraintsExpr(nb)(context, DirectType(IntType), currentBreed):::
                    List(TypeConstraint(found, DirectType(BreedSetType(getBreedFrom(expr).head)), expr))
            }
            case BreedAt(breed, x, y) => {
                genConstraintsExpr(x)(context, DirectType(IntType), currentBreed):::
                    genConstraintsExpr(y)(context, DirectType(IntType), currentBreed):::
                    List(TypeConstraint(found, DirectType(BreedSetType(getBreedFrom(breed).head)), expr))
            }
            case BreedAtSingle(breed, x, y) => {
                genConstraintsExpr(x)(context, DirectType(IntType), currentBreed):::
                    genConstraintsExpr(y)(context, DirectType(IntType), currentBreed):::
                    List(TypeConstraint(found, DirectType(BreedType(getBreedFrom(breed).head)), expr))
            }
            case BreedOn(breed, set) => {
                genConstraintsExpr(set)(context, DirectType(BreedSetType(getBreedFrom(set).head)), currentBreed):::
                    List(TypeConstraint(found, DirectType(BreedSetType(getBreedFrom(breed).head)), expr))
            }
            case Neighbors => List(TypeConstraint(found, DirectType(BreedSetType(context.getPatchBreed())), expr))
            case Other(breed) => genConstraintsExpr(breed)
            case Nobody => List(TypeConstraint(found, DirectType(BreedType(context.getAgentBreed())), expr))
            case OneOf(breed) => List(TypeConstraint(found, DirectType(BreedType(getBreedFrom(breed).head)), expr))

            case All(value, b, i) => {                
                TypeConstraint(found, DirectType(BoolType), expr)::
                genConstraintsExpr(b.predicateFilter(i))(context, DirectType(BoolType), b)
            }
            case Any(value) => {
                TypeConstraint(found, DirectType(BoolType), expr)::
                genConstraintsExpr(value)(context, DirectType(BreedSetType(getBreedFrom(value).head)), currentBreed)
            }
            case Count(value) => {
                TypeConstraint(found, DirectType(IntType), expr)::
                genConstraintsExpr(value)(context, DirectType(BreedSetType(getBreedFrom(value).head)), currentBreed)
            }

            // Arithmetic
            case ListValue(lst) => ???

            case IfElseBlockExpression(conds, block) => {
                conds.flatMap(c => 
                    genConstraintsExpr(c._1)(context, DirectType(BoolType), currentBreed) :::
                    genConstraintsExpr(c._2)
                ) ::: genConstraintsExpr(block)
            }

            case BinarayExpr(op, lf, rt) => {
                val varT = new TypedVariable()
                genConstraintsExpr(lf)(context, TypeOwn(varT), currentBreed) ::: 
                genConstraintsExpr(rt)(context, TypeOwn(varT), currentBreed) :::
                List(TypeConstraint(found, getBinaryOperationReturn(op, varT), expr))
            }

            case Call(fct, args) => {
                fct.getArguments().zip(args).flatMap{case (v, e) =>
                    genConstraintsExpr(e)(context, TypeOwn(v), currentBreed)
                } ::: List(TypeConstraint(found, TypeOwn(fct.returnVariable), expr))
            }
            case Ask_For_Report(variOwner, _, turtles, fct) =>
                val varT = new TypedVariable()
                genConstraintsExpr(turtles)(context, TypeOwn(varT), currentBreed) ::: 
                List(
                    TypeConstraint(found, ListOf(fct.returnVariable, varT), expr),
                    TypeConstraint(DirectType(BreedType(currentBreed)), getVariableConstraint(VariableValue(variOwner)), expr)
                )
        })
    }
    def getVariableConstraint(variLike: VariableLike) = {
        variLike match{
            case VariableValue(vari) => TypeOwn(vari)
            case OtherVariable(vari, _) => TypeOwn(vari)
        }
    }
    
    def getBinaryOperationReturn(op: String, varT: TypedVariable) = {
        op match{
            case "+" | "-" | "/" | "*" | "mod" | "and" | "or" | "xor" | "^" =>{
                TypeOwn(varT)
            }
            case "<" | "<=" | ">" | ">=" | "=" | "!=" =>{
                DirectType(BoolType)
            }
        }
    }

    /**
     * Check that All Breed Constraint matches and restrain function to breeds
     */ 
    private def resolveConstraits(constraints: List[TypeConstraint]):Boolean = {
        var globalChanged = false
        var changed = true
        while(changed){
            changed = constraints.map(it => resolveConstraint(it)).foldLeft(false)(_ || _)
            globalChanged |= changed
        }
        globalChanged
    }

    /**
     * Get the breed from a symtree expression
     */ 
    def getBreedFrom(expr: Expression)(implicit context: Context, breed: Breed): Set[Breed] = {
        expr match{
            case BreedValue(b) => Set(b)
            case Call(fct, args) => {
                fct match{
                    case cf: LinkedFunction => cf.returnValue.breeds
                    case bf: BaseFunction => {
                        bf.returnType match{
                            case BreedType(t) => Set(t)
                            case ListType(BreedType(t)) => Set(t)
                            case _ => throw new Exception(f"Function ${bf._name} does not return a breeds.")
                        }
                    }  
                }
            }
            case WithValue(expr, predicate, index) => getBreedFrom(expr)
            case SortBy(value, predicateBreed, sorterIndex) => Set(predicateBreed)
            case MinNAgent(expr, nb) => getBreedFrom(expr)
            case MaxNAgent(expr, nb) => getBreedFrom(expr)
            case v: VariableValue => {
                if (v.vari.name == "myself"){
                    Set(breed)
                } else {
                    v.vari.removeDuplicatedBreed();v.vari.breeds
                }
            }
            case BreedAt(b, x, y) => getBreedFrom(b)
            case BreedAtSingle(b, x, y) => getBreedFrom(b)
            case BreedOn(b, expr) => getBreedFrom(b)
            case Neighbors => Set(context.getPatchBreed())
            case Other(b) => getBreedFrom(b)
        }
    }

    private def resolveConstraint(it: TypeConstraint):Boolean = {
        var changed = false
        it.expected match{
            // Expect Type Set
            case DirectType(exp) => {
                it.found match{
                    case DirectType(found) => {
                        if (!found.hasAsParent(exp)){
                            throw TypeException(it.found, it.expected, it.position)
                        }
                    }
                    case TypeOwn(owner) => {
                        if (owner.canPutIn(exp)){
                            changed |= owner.putIn(exp)
                        }
                        else{
                            throw TypeException(it.found, it.expected, it.position)
                        }
                    }
                }
            }
            // Expect Type Owner
            case TypeOwn(ownerExp) => {
                it.found match{
                    case DirectType(found) => {
                        if (ownerExp.canPutIn(found)){
                            changed |= ownerExp.putIn(found)
                        }
                        else{
                            throw TypeException(it.found, it.expected, it.position)
                        }
                    }
                    case TypeOwn(owner) => {
                        if (owner.canPutIn(ownerExp)){
                            changed |= owner.putIn(ownerExp)
                            ownerExp.setDefaultType(owner.getType())
                        }
                        else{
                            throw TypeException(it.found, it.expected, it.position)
                        }
                    }
                }
            }
            // Expect Type Owner
            case ListOf(ownerExp, variable) => {
                variable.getType() match{
                    case BreedSetType(breed) => resolveConstraint(TypeConstraint(DirectType(ListType(ownerExp.getType())), it.found, it.position))
                    case BreedType(breed) => resolveConstraint(TypeConstraint(TypeOwn(ownerExp), it.found, it.position))
                    case _ => 
                }
            }
        }
        changed
    }
}