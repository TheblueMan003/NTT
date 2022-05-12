package ast

import scala.collection.mutable.Map
import analyser.Typed
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

    def getName() = Renamer.toValidName(name)
    def getGetterName() = f"get_${Renamer.toValidName(name)}"
    def getSetterName() = f"set_${Renamer.toValidName(name)}"
}