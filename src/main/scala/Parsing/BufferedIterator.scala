package parsing

import utils._

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
        string.substring(start, itIndex+1)
    }
    def hasNext(): Boolean = {
        itIndex < string.length
    }
}

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
        string.slice(start, itIndex+1)
    }
    def hasNext(): Boolean = {
        itIndex < string.length
    }
}