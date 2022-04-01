package utils

import scala.io.Source
import ast.{ Function, BaseFunction, Variable }
import ast.Breed
import analyser.Type
import scala.annotation.switch
import analyser.Types

object FunctionLoader{
    def getAll(breedName: String, breed: Breed): List[(String, Function)] = {
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
    def getFunction(line: String, breed: Breed):(String, Function) = {
        val fields = line.split(";")
        if (fields.size > 2){
            val name = fields(0)
            val args = fields(1).split(",").filter(_ != "").map(getArgument(_)).toList
            val ret = getType(fields(2))
            val func = BaseFunction(name, args, breed, ret)
            breed.addFunction(func)
            (name, func)
        }
        else{
            (null,null)
        }
    }
    def getArgument(line: String):Variable = {
        val fields = line.split(":")
        val vari = Variable(fields(0))
        vari.setType(getType(fields(1)))
        vari
    }
    def getType(string: String): Type = {
        string.toLowerCase match {
            case "int" => Types.IntType()
            case "float" => Types.FloatType()
            case "string" => Types.StringType()
            case "boolean" => Types.BoolType()
            case "unit" => Types.UnitType()
            case other => {
                if (other.startsWith("list[")){
                    Types.ListType(getType(other.substring(5, other.size-1)))
                }
                else if (other.startsWith("breed[")){
                    ???
                }
                else{
                    ???
                }
            }
        }
    }
}