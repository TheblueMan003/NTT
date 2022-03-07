package parsing

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