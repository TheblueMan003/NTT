package utils

import scala.io.Source
import netlogo.Variable
import netlogo.Breed
import netlogo.Type

object VariableLoader{
    def getAll(breedName: String, breed: Breed)(implicit context: Context): List[(String, Variable)] = {
        val path = f"base_breeds/variables/${breedName}.csv"
        if (getClass().getClassLoader().getResource(path) != null){
            Reporter.info(f"Loading Base Variables resources for: ${breedName}")
            Source.fromResource(path)
                .getLines
                .drop(1)
                .map(getVariable(_, breed))
                .filter(_ != (null,null))
                .toList
        }
        else{
            List()
        }
    }
    def getVariable(line: String, breed: Breed)(implicit context: Context):(String, Variable) = {
        val fields = line.split(";")
        if (fields.size > 1){
            val name = fields(0)
            val ret = Type.fromString(fields(1))
            val vari = new Variable(name, false)
            vari.setType(ret, true)
            breed.addVariable(vari)
            (name, vari)
        }
        else{
            (null,null)
        }
    }
}