package analyser

import ast.Breed

trait Type{
}
trait Typed{
    private var typ: Type = null
    def setType(typ: Type) = {
        this.typ = typ
    }
    def getType() = typ
}
object Types{
    case class IntType() extends Type
    case class FloatType() extends Type
    case class BoolType() extends Type
    case class StringType() extends Type
    case class BreedType(breed: Breed) extends Type
    case class BreedSetType(breed: Breed) extends Type
    case class ListType(inner: Type) extends Type
    case class UnitType() extends Type
}

trait TypeConstrainer{
    def getType(): Type
}
object TypeConstrainer{
    case class DirectType(set: Type) extends TypeConstrainer{
        override def getType() = set
    }
    case class TypeOwn(typed: Typed) extends TypeConstrainer{
        override def getType() = typed.getType()
    }
}
case class TypeConstraint(found: TypeConstrainer, expected: TypeConstrainer)