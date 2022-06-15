package utils
import scala.io.Source

object FileLoader{
    /**
      * Loads a file from the given path in the resources folder.
      *
      * @param path: The path of the file to load.
      * @return StringBufferedIterator of the file.
      */
    def load(path: String): StringBufferedIterator = {
        new StringBufferedIterator(Source.fromResource(path).getLines.reduce((x,y) => x + "\n" +y), path)
    }
}