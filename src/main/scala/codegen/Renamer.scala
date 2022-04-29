package codegen

object Renamer{
    def toValidName(string: String) = {
        string.replaceAll("[.?=*!<>:#+/%$_^'&-]","_")
    }

    def toClassName(string: String) = {
        toValidName(string).capitalize
    }

    def toGetterNane(string: String) = {
        "set_"+toValidName(string)
    }
}