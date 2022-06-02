package codegen

object Renamer{
    def toValidName(string: String) = {
        string.replaceAll("[.?=*!<>:#+/%$_^'&-]","_")
    }

    def toClassName(string: String) = {
        toValidName(string).capitalize
    }

    def toGetterNane(string: String) = {
        "get_"+toValidName(string)
    }
    def toSetterNane(string: String) = {
        "set_"+toValidName(string)
    }

    def concatLine(x: String, y: String, join: String = "\n"): String = {
        if (isJustSpace(x)){
            y
        }
        else if (isJustSpace(y)){
            x
        }
        else{
            x+"\n"+y
        }
    }

    def isJustSpace(x: String): Boolean = {
        x.trim() == ""
    }
}