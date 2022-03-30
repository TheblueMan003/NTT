package ast

import parsing.Token
import analyser.Type
import analyser.Types.UnitType
import scala.collection.mutable.Map
import analyser.SymTree

class Function(_name: String, _argsNames: List[String], _hasReturnValue: Boolean) extends BreedOwned{
    val name = _name
    val argsNames = _argsNames
    val hasReturnValue = _hasReturnValue
}

/**
 * Code defined Function that does not belong to a breed yet
 */
case class UnlinkedFunction(_name: String, _argsNames: List[String], tokenBody: List[Token], _hasReturnValue: Boolean) extends Function(_name, _argsNames, _hasReturnValue){
    var body: AST = null
    override def toString(): String = {
        s"${body} (${argsNames}){${body}}"
    }
    val returnValue = AST.VariableValue("return") // Used to capture return type breed constraints
    val possibleBreed = Set[Breed]()
}

/**
 * Code defined Function Before the Name Analysis
 */
case class LinkedASTFunction(_name: String, _argsNames: List[Variable], body: AST, breed: Breed, _hasReturnValue: Boolean) extends Function(_name, _argsNames.map(_._name), _hasReturnValue)

/**
 * Code defined Function After the Name Analysis
 */
case class LinkedSymFunction(_name: String, _argsNames: List[Variable], body: SymTree, breed: Breed, _hasReturnValue: Boolean) extends Function(_name, _argsNames.map(_._name), _hasReturnValue)


/**
 * Language predefined Function
 */ 
case class BaseFunction(_name: String, _argsNames: List[String], _breed: Breed, returnType: Type) extends Function(_name, _argsNames, returnType != UnitType()){
    initConstraints(Set(_breed))
}