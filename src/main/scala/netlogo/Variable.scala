package netlogo

import scala.collection.mutable.Map
import netlogo.Typed
import codegen.Renamer

trait VariableOwner{
    val ownedVars: Map[String, Variable] = Map[String, Variable]()

    def addVariable(name: String): Variable
    def addVariable(vari: Variable): Variable
    def hasVariable(name: String): Boolean
    def getVariable(name: String): Variable
    def getAllVariables(): Iterable[Variable] = ownedVars.values
}

class Variable(_name: String, val exported: Boolean = true) extends BreedOwned with Typed{
    val name = _name

    var hasGetterSetter = false

    def getName() = Renamer.toValidName(name)
    def getGetterName() = f"get_${Renamer.toValidName(name)}"
    def getSetterName() = f"set_${Renamer.toValidName(name)}"

    def getGetter() = if (hasGetterSetter){f"get_${Renamer.toValidName(name)}()"}else{Renamer.toValidName(name)}
    def getSetter(value: String) = if (hasGetterSetter){f"set_${Renamer.toValidName(name)}($value)"}else{f"${Renamer.toValidName(name)}=$value"}
}