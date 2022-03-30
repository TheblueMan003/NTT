package analyser

import ast.Breed

trait Type{
}
object Types{
    case class IntType() extends Type
    case class FloatType() extends Type
    case class BoolType() extends Type
    case class StringType() extends Type
    case class BreedType(breed: Breed) extends Type
    case class ListType(inner: Type) extends Type
    case class UnitType() extends Type

    case class VariableType(id: Int) extends Type
}

case class TypeConstraint(found: Type, expected: Type)