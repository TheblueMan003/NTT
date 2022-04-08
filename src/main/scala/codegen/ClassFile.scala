import scala.collection.mutable.ListBuffer

trait ResultFile{
    def getContent():String
}

class ClassFile() extends ResultFile{
    private val importList = ListBuffer[String]()
    
    def addImport(lib: String) = {
        importList.addOne(lib)
    }
}