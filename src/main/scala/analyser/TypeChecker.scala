package analyser

import SymTree._
import Types._
import TypeConstrainer._
import utils.Context
import ast.{Variable, LinkedFunction}

object TypeChecker{
    def analyse(context: Context) = {
        val constraints = genConstraints(context)

        resolveConstraits(constraints)
    }
    def genConstraints(context: Context): List[TypeConstraint] = {
        context.getBreeds().flatMap(_.getAllFunctions().flatMap(
            _ match{
                case lf: LinkedFunction => genConstraints(lf.symTree)(context)
                case _ => Nil
            }
        )).toList
    }
    def genConstraints(tree: SymTree)(implicit context: Context): List[TypeConstraint] = {
        tree match{
            case Call(fct, args) => {
                fct.getArguments().zip(args).flatMap{case (v, e) =>
                    genConstraintsExpr(e)(context, TypeOwn(v))
                }
            }
            case Assignment(vari, value) => genConstraintsExpr(value)(context, getVariableConstraint(vari))
            case Declaration(vari, value) => genConstraintsExpr(value)(context, getVariableConstraint(vari))
            case Block(block) => block.map(genConstraints(_)).foldLeft(List[TypeConstraint]())(_ ::: _)

            case IfBlock(cond, block) => {
                genConstraintsExpr(cond)(context,DirectType(BoolType())) ::: genConstraints(block)
            }
            case IfElseBlock(conds, block) => {
                conds.flatMap(c => 
                    genConstraintsExpr(c._1)(context, DirectType(BoolType())) :::
                    genConstraints(c._2)
                ) ::: genConstraints(block)
            }
            case Loop(block) => genConstraints(block)
            case Repeat(expr, block) => genConstraintsExpr(expr)(context,DirectType(IntType())) ::: genConstraints(block)
            case While(expr, block) => genConstraintsExpr(expr)(context,DirectType(BoolType())) ::: genConstraints(block)
            case Ask(_, turtles, block) => Nil
            case expr: Expression => throw new Exception(f"Lonely expression: ${expr}")
        }
    }
    def genConstraintsExpr(expr: Expression)(implicit context: Context, found: TypeConstrainer): List[TypeConstraint] = {
        expr match{
            case vl: VariableLike => List(TypeConstraint(found, getVariableConstraint(vl)))
            case BooleanValue(_) => List(TypeConstraint(found, DirectType(BoolType())))
            case IntValue(_) => List(TypeConstraint(found, DirectType(IntType())))
            case FloatValue(_) => List(TypeConstraint(found, DirectType(FloatType())))
            case StringValue(_) => List(TypeConstraint(found, DirectType(StringType())))
            case BreedValue(breed) => List(TypeConstraint(found, DirectType(BreedSetType(breed))))
            case ListValue(lst) => ???

            case IfElseBlockExpression(conds, block) => {
                conds.flatMap(c => 
                    genConstraintsExpr(c._1)(context, DirectType(BoolType())) :::
                    genConstraintsExpr(c._2)
                ) ::: genConstraintsExpr(block)
            }

            case BinarayExpr(op, lf, rt) => {
                val varT = new TypedVariable()
                genConstraintsExpr(lf)(context, TypeOwn(varT)) ::: 
                genConstraintsExpr(rt)(context, TypeOwn(varT)) :::
                List(TypeConstraint(found, getBinaryOperationReturn(op, varT)))
            }

            case Call(fct, args) => {
                fct.getArguments().zip(args).flatMap{case (v, e) =>
                    genConstraintsExpr(e)(context, TypeOwn(v))
                } ::: List(TypeConstraint(found, TypeOwn(fct.returnVariable)))
            }
        }
    }
    def getVariableConstraint(variLike: VariableLike) = {
        variLike match{
            case VariableValue(vari) => TypeOwn(vari)
            case OfValue(_, VariableValue(vari)) => TypeOwn(vari)
        }
    }
    def getBinaryOperationReturn(op: String, varT: TypedVariable) = {
        op match{
            case "+" | "-" | "/" | "*" | "mod" | "and" | "or" | "xor" =>{
                TypeOwn(varT)
            }
            case "<" | "<=" | ">" | ">=" | "=" | "!=" =>{
                DirectType(BoolType())
            }
        }
    }

    /**
     * Check that All Breed Constraint matches and restrain function to breeds
     */ 
    private def resolveConstraits(constraints: List[TypeConstraint]) = {
        var changed = true
        while(changed){
            changed = false
            constraints.map(it =>
                it.expected match{
                    // Expect Type Set
                    case DirectType(exp) => {
                        it.found match{
                            case DirectType(found) => {
                                if (!found.hasAsParent(exp)){
                                    throw TypeException(it.found, it.expected)
                                }
                            }
                            case TypeOwn(owner) => {
                                if (owner.canPutIn(exp)){
                                    changed |= owner.putIn(exp)
                                }
                                else{
                                    throw TypeException(it.found, it.expected)
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
                                    throw TypeException(it.found, it.expected)
                                }
                            }
                            case TypeOwn(owner) => {
                                if (owner.canPutIn(ownerExp)){
                                    changed |= owner.putIn(ownerExp)
                                }
                                else{
                                    throw TypeException(it.found, it.expected)
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}