package ast

import utils._
import parsing.Token
import scala.collection.mutable.{Map, Set}

class Tree extends Positionable{
}
class Expression extends Tree{

}
object Tree{
    case class BooleanValue(value: Boolean) extends Expression
    case class IntValue(value: Int) extends Expression
    case class FloatValue(value: Float) extends Expression
    case class StringValue(value: String) extends Expression

    case class Call(name: String, arg: List[Tree]) extends Expression
    case class Variable(name: String, owner: Breed) extends Expression
    case class UnlinkedVariable(name: String) extends Expression
    case class Assignment(name: Variable, value: Expression) extends Tree
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


class Breed(parent: Breed){
    val owned: Map[String, Tree.Variable] = Map[String, Tree.Variable]()

    def addVariable(name: String) = {
        owned.addOne((name, Tree.Variable(name, this)))
    }
    def hasVariable(name: String): Boolean = {
        if (owned.contains(name)){
            true
        }
        else if (parent != null){
            parent.hasVariable(name)
        }
        else{
            false
        }
    }
    def getVariable(name: String): Tree.Variable = {
        if (owned.contains(name)){
            owned.get(name).get
        }
        else if (parent != null){
            parent.getVariable(name)
        }
        else{
            throw new Exception(f"Unknown Variable: ${name}")
        }
    }
}

object Breed{
    case class TurtleBreed(singularName: String, pluralName: String, parent: Breed) extends Breed(parent)
    case class LinkBreed(singularName: String, pluralName: String, directed: Boolean, parent: Breed) extends Breed(parent)
    case class PatchBreed(singularName: String, pluralName: String, parent: Breed) extends Breed(parent)
    case class Observer() extends Breed(null)
}

class BreedType
object BreedType{
    case class TurtleBreed() extends BreedType
    case class LinkBreed(directed: Boolean) extends BreedType
    case class PatchBreed() extends BreedType
    case class Observer() extends BreedType
}