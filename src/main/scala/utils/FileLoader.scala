package utils
import scala.io.Source

object FileLoader{
    def load(path: String): StringBufferedIterator = {
        new StringBufferedIterator(Source.fromResource(path).getLines.reduce((x,y) => x + "\n" +y), path)
    }
}