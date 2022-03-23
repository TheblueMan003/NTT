package ast

import utils._
import parsing.Token
import scala.collection.mutable.{Map, Set}

trait Tree extends Positionable
trait Expression extends Tree

object Tree{
    case class BooleanValue(value: Boolean) extends Expression
    case class IntValue(value: Int) extends Expression
    case class FloatValue(value: Float) extends Expression
    case class StringValue(value: String) extends Expression
    case class VariableValue(value: String) extends Expression

    case class Call(name: String, arg: List[Tree]) extends Expression
    case class Assignment(name: VariableValue, value: Expression) extends Tree
    case class Declaration(name: VariableValue, value: Expression) extends Tree
    case class BinarayExpr(op: String, lh: Expression, rh: Expression) extends Expression
    case class Block(body: List[Tree]) extends Tree

    case class IfBlock(cond: Expression, block: Tree) extends Tree
    case class IfElseBlock(blocks: List[(Expression, Tree)], elseBlock: Tree) extends Tree
    case class IfElseBlockExpression(blocks: List[(Expression, Tree)], elseBlock: Tree) extends Expression

    case class Loop(block: Tree) extends Tree
    case class Repeat(number: Expression, block: Tree) extends Tree
    case class While(cond: Expression, block: Tree) extends Tree
    case class Ask(block: Tree) extends Tree
}


