package analyser

import ast.Breed

trait Typed{
    private var typ: Type = null
    def setType(typ: Type) = {
        this.typ = typ
    }
    def getType() = typ

    /**
     * Restraint the object to the type
     * 
     * @return true if type set changed
     */
    def restrainTypeTo(ntyp: Type):Boolean={
        val ret = typ != ntyp
        typ = ntyp
        ret
    }

    /**
     * Restraint the object to the type
     * 
     * @return true if type changed
     */
    def restrainTypeTo(other: Typed):Boolean={
        if (other.typ == null){
            false
        }
        else{
            val ret = typ != other.typ
            typ = other.typ
            ret
        }
    }

    /**
     * Return if the object can be specialized for the type
     */ 
    def canTypeBeRestrainTo(ntyp: Type): Boolean = {
        if (typ == null){
            true
        }
        else{
            typ.canBeRestrainTo(ntyp)
        }
    }

    /**
     * Return if the object can be specialized for the type
     */ 
    def canTypeBeRestrainTo(other: Typed): Boolean = {
        if (typ == null){
            true
        }
        else{
            typ.canBeRestrainTo(other.typ)
        }
    }
}
abstract class Type(_parent: Type){
    val parent = _parent
    def canBeRestrainTo(other: Type): Boolean = {
        if (other == null){
            true
        }
        else if (other == this){
            true
        }
        else if (other.parent != null){
            this.canBeRestrainTo(other.parent)
        }
        else{
            false
        }
    }

}
object Types{
    case class IntType() extends Type(FloatType())
    case class FloatType() extends Type(UnitType())
    case class BoolType() extends Type(UnitType())
    case class StringType() extends Type(UnitType())
    case class BreedType(breed: Breed) extends Type(UnitType())
    case class BreedSetType(breed: Breed) extends Type(UnitType())
    case class ListType(inner: Type) extends Type(UnitType())
    case class UnitType() extends Type(null)
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
class TypedVariable() extends Typed
case class TypeConstraint(found: TypeConstrainer, expected: TypeConstrainer)

case class TypeException(found: TypeConstrainer, expect: TypeConstrainer) extends Exception{
    override def getMessage():String = {
        return f"Expected: ${expect.getType()} Found: ${found.getType()}"
    }
}