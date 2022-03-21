package analyser

import ast.Breed

abstract class Type{
}
object Types{
    case class IntType() extends Type
    case class FloatType() extends Type
    case class BoolType() extends Type
    case class StringType() extends Type
    case class TurtleType(breed: Breed) extends Type


    case class VariableType(id: Int) extends Type
}

abstract class Constraint{
}
object Constraints{
    case class TypeConstraint(found: Type, expected: Type) extends Constraint
    case class BreedConstraint(found: Breed, expected: Set[Breed]) extends Constraint
}