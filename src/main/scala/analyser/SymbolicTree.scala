package analyser

import utils._
import ast.{Variable, Function, Breed}
import scala.collection.mutable.{Map, Set}

trait SymTree extends Positionable

object SymTree{
    trait Expression extends SymTree
    trait VariableLike extends Expression

    case class BooleanValue(value: Boolean) extends Expression
    case class IntValue(value: Int) extends Expression
    case class FloatValue(value: Float) extends Expression
    case class StringValue(value: String) extends Expression
    case class VariableValue(vari: Variable) extends VariableLike
    case class BreedValue(name: Breed) extends Expression
    case class ListValue(lst: List[Expression]) extends Expression
    case class OfValue(expr: VariableValue, from: Expression) extends VariableLike
    case class WithValue(value: Expression, predicate: Expression) extends Expression

    case class Call(fct: Function, arg: List[Expression]) extends Expression
    case class CreateBreed(breed: BreedValue, nb: Expression, fct: Function) extends SymTree
    case class Assignment(vari: VariableLike, value: Expression) extends SymTree
    case class Declaration(vari: VariableLike, value: Expression) extends SymTree
    case class BinarayExpr(op: String, lh: Expression, rh: Expression) extends Expression
    case class Block(body: List[SymTree]) extends SymTree
    case class List(body: List[SymTree]) extends SymTree
    case class Report(expr: Expression) extends SymTree

    case class IfBlock(cond: Expression, block: SymTree) extends SymTree
    case class IfElseBlock(blocks: List[(Expression, SymTree)], elseBlock: SymTree) extends SymTree
    case class IfElseBlockExpression(blocks: List[(Expression, Expression)], elseBlock: Expression) extends Expression

    case class Loop(block: SymTree) extends SymTree
    case class Repeat(number: Expression, block: SymTree) extends SymTree
    case class While(cond: Expression, block: SymTree) extends SymTree
    case class Ask(upperCaller: List[Variable], turtles: Expression, block: Function) extends SymTree
    
    case object Tick extends SymTree
    case object Empty extends SymTree
}