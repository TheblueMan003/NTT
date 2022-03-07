package ast

import utils._
import parsing.Token

class Tree extends Positionable{
}
object Tree{
    case class Call(name: String, arg: List[Tree])
    case class Global(name: String, value: Tree)
    case class Owned(breed: String, name: String, value: Tree)
    case class Block(body: List[Tree])
    case class FunctionDef(name: String, args: List[Tree], body: Tree)
    
    case class UnfinishedFunctionDef(name: String, args: List[Token], unparsed: List[Token])
}