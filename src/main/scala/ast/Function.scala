package ast

import parsing.Token

trait BreedOwned{
}
class Function(_name: String, _argsNames: List[String]) extends BreedOwned{
    val name = _name
    val argsNames = _argsNames
    var breeds = Set[Breed]()

    def initConstraints(constraints: Set[Breed]){
        breeds = constraints
    }
    def addBreedConstraints(constraints: Set[Breed]){
        breeds = breeds.intersect(constraints)
    }
}
case class CompiledFunction(_name: String, _argsNames: List[String], tokenBody: List[Token]) extends Function(_name, _argsNames){
    var body: Tree = null
    override def toString(): String = {
        s"${body} (${argsNames}){${body}}"
    }

    val possibleBreed = Set[Breed]()
}
case class BaseFunction(_name: String, _argsNames: List[String]) extends Function(_name, _argsNames)