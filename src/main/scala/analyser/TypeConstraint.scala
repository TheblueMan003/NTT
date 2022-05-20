package analyser

import netlogo.Breed
import utils.Context
import netlogo.{Type, Types, Typed}


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
    case class ListOf(typed: Typed) extends TypeConstrainer{
        override def getType() = Types.ListType(typed.getType())
    }
}
class TypedVariable() extends Typed
case class TypeConstraint(found: TypeConstrainer, expected: TypeConstrainer)

case class TypeException(found: TypeConstrainer, expect: TypeConstrainer) extends Exception{
    override def getMessage():String = {
        return f"Expected: ${expect.getType()} Found: ${found.getType()}"
    }
}