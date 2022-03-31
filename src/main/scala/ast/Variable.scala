package ast

import scala.collection.mutable.Map
import analyser.Typed

trait VariableOwner{
    val ownedVars: Map[String, Variable] = Map[String, Variable]()

    def addVariable(name: String): Variable
    def hasVariable(name: String): Boolean
    def getVariable(name: String): Variable
    def getAllVariables(): Iterable[Variable] = ownedVars.values
}

case class Variable(_name: String) extends BreedOwned with Typed{
}