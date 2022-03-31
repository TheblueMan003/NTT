package analyser

import SymTree._
import Types._
import TypeConstrainer._
import utils.Context

object TypeChecker{
    def analyse() = {

    }
    def genConstraints(expr: Expression)(implicit context: Context, found: TypeConstrainer): List[TypeConstraint] = {
        expr match{
            case BooleanValue(_) => List(TypeConstraint(found, DirectType(BoolType())))
            case IntValue(_) => List(TypeConstraint(found, DirectType(IntType())))
            case FloatValue(_) => List(TypeConstraint(found, DirectType(FloatType())))
            case StringValue(_) => List(TypeConstraint(found, DirectType(StringType())))
            case BreedValue(breed) => List(TypeConstraint(found, DirectType(BreedSetType(breed))))
            case OfValue(expr, from) => ???
        }
    }
}