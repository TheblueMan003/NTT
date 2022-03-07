package utils

import ast.Tree._
import scala.collection.mutable.Map

object Context{
    val functions = Map[String, Function]()
}

class Function(name: String, argsNames: List[String]){
}