package ast

import parsing.Token
import analyser.Type
import analyser.Types.UnitType


class Function(_name: String, _argsNames: List[String], _hasReturnValue: Boolean) extends BreedOwned{
    val name = _name
    val argsNames = _argsNames
    val hasReturnValue = _hasReturnValue
}

/**
 * Code defined Function
 */
case class CompiledFunction(_name: String, _argsNames: List[String], tokenBody: List[Token], _hasReturnValue: Boolean) extends Function(_name, _argsNames, _hasReturnValue){
    var body: AST = null
    override def toString(): String = {
        s"${body} (${argsNames}){${body}}"
    }

    val possibleBreed = Set[Breed]()
    val returnValue = AST.VariableValue("return")
}

/**
 * Language predefined Function
 */ 
case class BaseFunction(_name: String, _argsNames: List[String], _breed: Breed, returnType: Type) extends Function(_name, _argsNames, returnType != UnitType()){
    initConstraints(Set(_breed))
}