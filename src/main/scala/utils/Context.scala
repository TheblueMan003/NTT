package utils

import ast.Tree._
import scala.collection.mutable.Map
import parsing.Token
import javax.swing.text.StyledEditorKit.BoldAction

trait VariableOwner{
}
object VariableOwner{
    case class Global() extends VariableOwner
    case class TurtlesOwned() extends VariableOwner
    case class PatchesOwned() extends VariableOwner
}

class Context{
    val functions = Map[String, Function]()
    val variables = Map[String, Variable]()

    def addFunction(name: String, args: List[String], body: List[Token]) = {
        functions.addOne((name, new Function(name, args, body)))
    }
    def hasFunction(name: String): Boolean = functions.contains(name)
    def getFunction(name: String): Function = functions.get(name).get

    def addVariable(name: String, owner: VariableOwner) = {
        variables.addOne((name, new Variable(name, owner)))
    }
    def hasVariable(name: String): Boolean = variables.contains(name)
    def getVariable(name: String): Variable = variables.get(name).get
}

class Function(name: String, argsNames: List[String], body: List[Token]){
}
class Variable(name: String, owner: VariableOwner){
    def getName() = name
    def getOwner() = owner 
}