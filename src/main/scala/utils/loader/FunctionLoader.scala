package utils

import scala.io.Source
import ast.{ Function, BaseFunction, Variable }
import ast.Breed
import analyser.Type
import analyser.Types

object FunctionLoader{
    def getAll(breedName: String, breed: Breed)(implicit context: Context): List[(String, Function)] = {
        val path = f"base_breeds/functions/${breedName}.csv"
        if (getClass().getClassLoader().getResource(path) != null){
            Reporter.info(f"Loading Base Function resources for: ${breedName}")
            Source.fromResource(path)
                .getLines
                .drop(1)
                .map(getFunction(_, breed))
                .filter(_ != (null,null))
                .toList
        }
        else{
            List()
        }
    }
    def getFunction(line: String, breed: Breed)(implicit context: Context):(String, Function) = {
        val fields = line.split(";")
        if (fields.size > 2){
            val name = fields(0)
            val args = fields(1).split(",").filter(_ != "").map(getArgument(_)).toList
            val ret = Type.fromString(fields(2))
            val func = BaseFunction(name, args, breed, ret, fields(3))
            breed.addFunction(func)
            (name, func)
        }
        else{
            (null,null)
        }
    }
    def getArgument(line: String)(implicit context: Context):Variable = {
        val fields = line.split(":")
        val vari = new Variable(fields(0))
        vari.setType(Type.fromString(fields(1)), true)
        vari
    }
}