package analyser

import ast.Breed

trait Typed{
    private var typ: Type = null
    var typeFixed: Boolean = false

    def setType(typ: Type, fixed: Boolean = false) = {
        this.typ = typ
        typeFixed = fixed
    }
    def getType() = typ

    /**
     * Change for the type for ntyp. Return true if different from current one.
     * 
     */ 
    def changeTypeFor(ntyp: Type):Boolean = {
        val ret = typ != ntyp
        typ = ntyp
        ret
    }

    /**
     * Restraint the object to the type
     * 
     * @return true if type set changed
     */
    def putIn(ntyp: Type):Boolean={
        if (ntyp == null){
            false
        }
        else if (typ == null){
            changeTypeFor(ntyp)
        }
        else if (typ.isParentOf(ntyp)){
            false
        }
        else if (!typeFixed && ntyp.isParentOf(this.typ)){
            changeTypeFor(ntyp)
        }
        else{
            throw new IllegalStateException("Object Type Cannot be change.")
        }
    }

    /**
     * Restraint the object to the type
     * 
     * @return true if type changed
     */
    def putIn(other: Typed):Boolean={
        putIn(other.typ)
    }

    /**
     * Return if the object can be specialized for the type
     */ 
    def canPutIn(ntyp: Type): Boolean = {
        if (typ == null){
            true
        }
        else if (typ.isParentOf(ntyp)){
            true
        }
        else if (!typeFixed && ntyp.isParentOf(this.typ)){
            true
        }
        else{
            false
        }
    }

    /**
     * Return if the object can be specialized for the type
     */ 
    def canPutIn(other: Typed): Boolean = {
        canPutIn(other.typ)
    }
}
abstract class Type(_parent: Type){
    val parent = _parent

    def hasAsParent(other: Type): Boolean = {
        if (other == null){
            true
        }
        else if (other == this){
            true
        }
        else if (parent != null){
            parent.hasAsParent(other)
        }
        else{
            false
        }
    }

    def isParentOf(other: Type): Boolean = {
        if (other == null){
            true
        }
        else if (other == this){
            true
        }
        else if (other.parent != null){
            isParentOf(other.parent)
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