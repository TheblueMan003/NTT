class StringBufferedIterator(string: String){
    var itIndex = 0
    var start = -1

    def take():Char = {
        val c = string[itIndex]
        itIndex ++
        c
    }
    def peek():Char ={
        string[itIndex]
    }
    def setStart():Char = {
        start = itIndex
    }
    def cut(): String = {
        string.substring(start, itIndex)
    }
    def hasNext(): Boolean = {
        index < string.length
    }
}

extension (c: Char)
  def isWordChar: Boolean = c.isLetterOrDigit || c == '-'