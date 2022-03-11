package parsing

import utils._
import parsing.Tokens._

class StringBufferedIterator(string: String) extends Positionable{
    var itIndex = 0
    var start = -1

    def take():Char = {
        val c = string(itIndex)
        itIndex += 1
        c
    }
    def takeWhile(predicate: Char=>Boolean) = {
        while (hasNext() && predicate(peek())){
            take()
        }
    }
    def peek():Char ={
        string(itIndex)
    }
    def setStart() = {
        start = itIndex
    }
    def cut(): String = {
        string.substring(start, itIndex)
    }
    def hasNext(): Boolean = {
        itIndex < string.length
    }
}

case class UnexpectedTokenException(val actual: Token, val expected: Token) extends Exception(actual.toString() + "!=" + expected.toString())
case class UnexpectedEOFException() extends Exception

class TokenBufferedIterator(string: List[Token]) extends Positionable{
    var itIndex = 0
    var start = -1

    def take():Token = {
        val c = string(itIndex)
        itIndex += 1
        c
    }
    def takeWhile(predicate: Token=>Boolean) = {
        while (hasNext() && predicate(peek())){
            take()
        }
    }
    def peek():Token ={
        string(itIndex)
    }
    def setStart() = {
        start = itIndex
    }
    def cut(): List[Token] = {
        string.slice(start, itIndex)
    }
    def hasNext(): Boolean = {
        itIndex < string.length
    }
    def requierToken(token: Token) = {
        if (!hasNext()){
            throw new UnexpectedEOFException()
        }
        else if (peek() != token){
            throw new UnexpectedTokenException(peek(), token)
        }
        else{
            take()
        }
    }
    def getIdentifier(): String = {
        if (!hasNext()){
            throw new UnexpectedEOFException()
        }
        else{
            take() match{
                case IdentifierToken(name) => name
                case token => throw new UnexpectedTokenException(token, IdentifierToken("_"))
            }
        }
    }
}