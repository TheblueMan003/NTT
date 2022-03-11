package ast

import utils._
import parsing.Token

class Tree extends Positionable{
}
object Tree{
    case class Call(name: String, arg: List[Tree]) extends Tree
    case class Variable(name: String, owner: VariableOwner) extends Tree
    case class Block(body: List[Tree]) extends Tree
    case class FunctionDef(name: String, args: List[Tree], body: Tree) extends Tree
}