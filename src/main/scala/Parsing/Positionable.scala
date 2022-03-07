trait Positionable{
    val index: Int
    val line: Int

    def setPosition(index: Int, line: Int) = {
        this.index = index
        this.line = line
    }
    def setPosition(other: Positionable) = {
        this.index = other.index
        this.line = other.line
    }
}