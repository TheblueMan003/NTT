package analyser

trait Type{
}
object Types{
    case class IntType() extends Type
    case class FloatType() extends Type
    case class BoolType() extends Type
    case class StringType() extends Type
    case class TurtleType(breed: Breed) extends Type


    case class VariableType(id: Int) extends Type
}

trait TypeConstraint{
}
object TypeConstraints{
    case class TypeConstraint(found: Type, expected: Type) extends Constraint
}
