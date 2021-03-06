package utils

import parsing.Tokens._

class StringBufferedIterator(string: String, filename: String){
    var itIndex = 0
    var start = -1

    private var startLineIndex = -1
    private var startLine = -1
    
    private var lineIndex = 0
    private var line = 1

    /**
     * @return the next char and consume it
     */
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

    /**
      * Consume char with predicate is true
      *
      * @param predicate: (Char) => Boolean the predicate to test
      */
    def takeWhile(predicate: Char=>Boolean) = {
        while (hasNext() && predicate(peek())){
            take()
        }
    }

    /**
      * @return the next char
      */
    def peek():Char ={
        string(itIndex)
    }

    /**
      * Set the start of the cut here
      */
    def setStart() = {
        start = itIndex
        startLine = line
        startLineIndex = lineIndex
    }
    /**
      * @return the string from the start of the cut to here
      */
    def cut(): (String, Positionable) = {
        (string.substring(start, itIndex), Position(startLineIndex, startLine, filename))
    }

    /**
      * @return true if there is a next char
      */
    def hasNext(): Boolean = {
        itIndex < string.length
    }
}