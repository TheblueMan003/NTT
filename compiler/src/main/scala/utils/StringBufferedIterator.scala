package utils

import parsing.Tokens._

class StringBufferedIterator(string: String, filename: String){
    var itIndex = 0
    var start = -1

    private var startLineIndex = -1
    private var startLine = -1
    
    private var lineIndex = 0
    private var line = 1

    def take():Char = {
        val c = string(itIndex)
        itIndex += 1
        if (c == '\n'){
            lineIndex = 0
            line += 1
        }
        else{
            lineIndex += 1
        }
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
        startLine = line
        startLineIndex = lineIndex
    }
    def cut(): (String, Positionable) = {
        (string.substring(start, itIndex), Position(startLineIndex, startLine, filename))
    }
    def hasNext(): Boolean = {
        itIndex < string.length
    }
}