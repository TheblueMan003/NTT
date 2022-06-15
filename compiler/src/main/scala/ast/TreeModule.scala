package ast

import utils._
import parsing.Token
import scala.collection.mutable.{Map, Set}
import netlogo._

trait AST extends Positionable
object AST{
    trait Expression extends AST

    case object Empty extends AST

    case class BooleanValue(value: Boolean) extends Expression
    case class IntValue(value: Int) extends Expression
    case class FloatValue(value: Float) extends Expression
    case class StringValue(value: String) extends Expression
    case class VariableValue(name: String) extends Expression with BreedOwned
    case class BreedValue(name: Breed) extends Expression
    case class ListValue(lst: List[Expression]) extends Expression
    case class OneOfValue(lst: List[Expression]) extends Expression
    case class Not(value: Expression) extends Expression
    case object Nobody extends Expression

    case class All(breed: Expression, predicate: Expression) extends Expression
    case class Any(breed: Expression) extends Expression
    case class Count(breed: Expression) extends Expression

    case class OfValue(expr: Expression, from: Expression) extends Expression
    case class WithValue(value: Expression, predicate: Expression) extends Expression
    case class SortBy(value: Expression, sorter: Expression) extends Expression
    case class MinOneAgent(value: Expression) extends Expression
    case class MaxOneAgent(value: Expression) extends Expression
    case class MinNAgent(value: Expression, number: Expression) extends Expression
    case class MaxNAgent(value: Expression, number: Expression) extends Expression
    case class BreedAt(breed: Expression, x: Expression, y: Expression) extends Expression
    case class BreedAtSingle(breed: Expression, x: Expression, y: Expression) extends Expression
    case class BreedOn(breed: Expression, set: Expression) extends Expression
    case class Other(breed: Expression) extends Expression
    case class OneOf(breed: Expression) extends Expression
    case object Neighbors extends Expression

    case class Call(name: String, arg: List[Expression]) extends Expression
    case class Assignment(name: VariableValue, value: Expression) extends AST
    case class Declaration(name: VariableValue, value: Expression) extends AST
    case class BinarayExpr(op: String, lh: Expression, rh: Expression) extends Expression
    case class Block(body: List[AST]) extends AST
    case class Report(expr: Expression) extends AST

    case class CreateBreed(breed: BreedValue, nb: Expression, arg: Block) extends AST
    case class HatchBreed(breed: BreedValue, nb: Expression, arg: Block) extends AST

    case class IfBlock(cond: Expression, block: AST) extends AST
    case class IfElseBlock(blocks: List[(Expression, AST)], elseBlock: AST) extends AST
    case class IfElseBlockExpression(blocks: List[(Expression, Expression)], elseBlock: Expression) extends Expression

    case class Loop(block: AST) extends AST
    case class Repeat(number: Expression, block: AST) extends AST
    case class While(cond: Expression, block: AST) extends AST
    case class Ask(turtles: Expression, block: AST) extends AST
}