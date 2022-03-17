package ast

import utils._
import parsing.Token

class Tree extends Positionable{
}
class Expression extends Tree{

}
object Tree{
    case class Call(name: String, arg: List[Tree]) extends Expression
    case class Variable(name: String, owner: VariableOwner) extends Expression
    case class BinarayExpr(op: String, lh: Expression, rh: Expression) extends Expression
    case class Block(body: List[Tree]) extends Tree
    case class FunctionDef(name: String, args: List[Tree], body: Tree) extends Tree
}