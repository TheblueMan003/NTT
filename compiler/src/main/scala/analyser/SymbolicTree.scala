package analyser

import utils._
import netlogo.{Variable, Function, Breed, Typed}
import scala.collection.mutable.{Map, Set}

trait SymTree extends Positionable

object SymTree{
    trait Expression extends SymTree with Typed
    trait VariableLike extends Expression

    case class DirectValue(value: String) extends Expression
    case class BooleanValue(value: Boolean) extends Expression
    case class IntValue(value: Int) extends Expression
    case class FloatValue(value: Float) extends Expression
    case class StringValue(value: String) extends Expression
    case class VariableValue(vari: Variable) extends VariableLike
    case class BreedValue(name: Breed) extends Expression
    case class ListValue(lst: List[Expression]) extends Expression
    case class OtherVariable(value: Variable, indexStack: Int) extends VariableLike
    case class OneOfValue(lst: List[Expression]) extends Expression
    case class Not(value: Expression) extends Expression

    case class WithValue(value: Expression, predicateBreed: Breed, predicateIndex: Int) extends Expression
    case class SortBy(value: Expression, predicateBreed: Breed, sorterIndex: Int) extends Expression
    case class MinOneAgent(value: Expression) extends Expression
    case class MaxOneAgent(value: Expression) extends Expression
    case class MinNAgent(value: Expression, number: Expression) extends Expression
    case class MaxNAgent(value: Expression, number: Expression) extends Expression
    case class BreedAt(breed: Expression, x: Expression, y: Expression) extends Expression
    case class BreedAtSingle(breed: Expression, x: Expression, y: Expression) extends Expression
    case class BreedOn(breed: Expression, set: Expression) extends Expression
    case object Neighbors extends Expression
    case object Nobody extends Expression
    case class Other(breed: Expression) extends Expression
    case class OneOf(breed: Expression) extends Expression

    case class All(value: Expression, predicateBreed: Breed, predicateIndex: Int) extends Expression
    case class Any(value: Expression) extends Expression
    case class Count(value: Expression) extends Expression


    case class Call(fct: Function, arg: List[Expression]) extends Expression
    case class CreateBreed(breed: BreedValue, nb: Expression, fct: Function) extends SymTree
    case class HatchBreed(breed: BreedValue, nb: Expression, fct: Function) extends SymTree
    case class Assignment(vari: VariableLike, value: Expression) extends SymTree
    case class Declaration(vari: VariableLike, value: Expression) extends SymTree
    case class BinarayExpr(op: String, lh: Expression, rh: Expression) extends Expression
    case class Block(body: List[SymTree]) extends SymTree
    case class Report(expr: Expression) extends SymTree

    case class IfBlock(cond: Expression, block: SymTree) extends SymTree
    case class IfElseBlock(blocks: List[(Expression, SymTree)], elseBlock: SymTree) extends SymTree
    case class IfElseBlockExpression(blocks: List[(Expression, Expression)], elseBlock: Expression) extends Expression

    case class Loop(block: SymTree) extends SymTree
    case class Repeat(number: Expression, block: SymTree) extends SymTree
    case class While(cond: Expression, block: SymTree) extends SymTree
    case class Ask(myself: Variable, breed: Breed, turtles: Expression, block: Function) extends SymTree
    case class Ask_For_Report(myself: Variable, breed: Breed, turtles: Expression, block: Function) extends Expression
}