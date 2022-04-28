package analyser

import ast.Breed
import utils.Context

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
object Type{
    def toString(typ: Type):String = {
        typ match {
            case Types.BoolType => "Boolean"
            case Types.FloatType => "Float"
            case Types.IntType => "Int"
            case Types.ListType(inner) => f"List[${toString(inner)}]"
            case Types.StringType => "String"
            case Types.UnitType => "Unit"
            case null => "Int"
        }
    }
    def defaultValue(typ: Type):String = {
        typ match {
            case Types.BoolType => "false"
            case Types.FloatType => "0"
            case Types.IntType => "0"
            case Types.ListType(inner) => f"Nil"
            case Types.StringType => "null"
            case null => "0"
        }
    }
    def fromString(string: String)(implicit context: Context): Type = {
        string.toLowerCase match {
            case "int" => Types.IntType
            case "float" => Types.FloatType
            case "string" => Types.StringType
            case "boolean" => Types.BoolType
            case "unit" => Types.UnitType
            case other => {
                if (other.startsWith("list[")){
                    Types.ListType(fromString(other.substring(5, other.size-1)))
                }
                else if (other.startsWith("breedset[")){
                    Types.BreedSetType(context.getBreedPlural(other.substring(9, other.size-1)))
                }
                else if (other.startsWith("breed[")){
                    Types.BreedType(context.getBreedSingular(other.substring(6, other.size-1)))
                }
                else{
                    ???
                }
            }
        }
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
    case object IntType extends Type(FloatType)
    case object FloatType extends Type(UnitType)
    case object BoolType extends Type(UnitType)
    case object StringType extends Type(UnitType)
    case class BreedType(breed: Breed) extends Type(UnitType)
    case class BreedSetType(breed: Breed) extends Type(UnitType)
    case class ListType(inner: Type) extends Type(UnitType)
    case object UnitType extends Type(null)
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