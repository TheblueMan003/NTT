package ast

import parsing.Token


class Function(_name: String, _argsNames: List[String]) extends BreedOwned{
    val name = _name
    val argsNames = _argsNames
}
case class CompiledFunction(_name: String, _argsNames: List[String], tokenBody: List[Token]) extends Function(_name, _argsNames){
    var body: AST = null
    override def toString(): String = {
        s"${body} (${argsNames}){${body}}"
    }

    val possibleBreed = Set[Breed]()
}
case class BaseFunction(_name: String, _argsNames: List[String], _breed: Breed) extends Function(_name, _argsNames){
    initConstraints(Set(_breed))
}