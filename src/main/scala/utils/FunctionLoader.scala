package utils

import scala.io.Source
import ast.{ Function, BaseFunction }
import ast.Breed

object FunctionLoader{
    def getAll(breedName: String, breed: Breed): List[(String, Function)] = {
        val path = "base_breed_function/"+breedName+".csv"
        if (getClass().getClassLoader().getResource(path) != null){
            println(f"Loading Base Function resources for: ${breedName}")
            Source.fromResource(path)
                .getLines
                .map(_.split(";"))
                .filter(_.size > 1)
                .map(a => (a(0), BaseFunction(a(0), a(1).split(",").toList, breed)))
                .toList
        }
        else{
            List()
        }
    }
}