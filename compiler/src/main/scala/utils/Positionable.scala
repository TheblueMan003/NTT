package utils

trait Positionable{
    var index: Int = 0
    var line: Int = 0
    var file: String = ""

    /**
      * Set the position of the element
      *
      * @param index
      * @param line
      */
    def setPosition(index: Int, line: Int) = {
        this.index = index
        this.line = line
    }
    /**
      * set the position of the element
      *
      * @param other: the position to set
      */
    def setPosition(other: Positionable) = {
        this.index = other.index
        this.line = other.line
        this.file = other.file
    }
    /**
      * @return the position of the element in the source file
      */
    def positionString():String = {
        f"in file ${file} at line ${line} at index ${index}"
    }
}

/**
  * Position inside a source file
  *
  * @param _index: the column of the element in the source file
  * @param _line: the line of the element in the source file
  * @param _file: the file of the element
  */
case class Position(_index: Int, _line: Int, _file: String) extends Positionable{
    index = _index
    line = _line
    file = _file
}