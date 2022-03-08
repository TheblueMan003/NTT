package utils

import ast.Tree._
import scala.collection.mutable.Map
import parsing.Token

trait VariableOwner{
}
object VariableOwner{
    case class Global() extends VariableOwner
    case class TurtlesOwned() extends VariableOwner
    case class PatchesOwned() extends VariableOwner
}

class Context{
    val functions = Map[String, Function]()
    val variable = Map[String, Variable]()

    def addFunction(name: String, args: List[String], body: List[Token]) = {
        functions.addOne((name, new Function(name, args, body)))
    }
    def addVariable(name: String, owner: VariableOwner) = {
        variable.addOne((name, new Variable(name, owner)))
    }
}

class Function(name: String, argsNames: List[String], body: List[Token]){
}
class Variable(name: String, owner: VariableOwner){
}