package utils

trait Positionable{
    var index: Int = 0
    var line: Int = 0

    def setPosition(index: Int, line: Int) = {
        this.index = index
        this.line = line
    }
    def setPosition(other: Positionable) = {
        this.index = other.index
        this.line = other.line
    }
}