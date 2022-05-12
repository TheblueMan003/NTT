package utils

trait Positionable{
    var index: Int = 0
    var line: Int = 0
    var file: String = ""

    def setPosition(index: Int, line: Int) = {
        this.index = index
        this.line = line
    }
    def setPosition(other: Positionable) = {
        this.index = other.index
        this.line = other.line
        this.file = other.file
    }
    def positionString():String = {
        f"in file ${file} at line ${line} at index ${index}"
    }
}
case class Position(_index: Int, _line: Int, _file: String) extends Positionable{
    index = _index
    line = _line
    file = _file
}