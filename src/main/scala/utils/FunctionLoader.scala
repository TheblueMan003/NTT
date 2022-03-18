package utils

import scala.io.Source
import utils.Function
import ast.Breed

object FunctionLoader{
    def getAll(breed: String): List[(String, Function)] = {
        Source.fromResource("base_breed_function/"+breed+".csv")
              .getLines
              .map(_.split(";"))
              .map(a => (a(0), BaseFunction(a(0), a(1).split(",").toList)))
              .toList
    }
}