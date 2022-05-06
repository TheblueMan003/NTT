package ast

import parsing.Token
import analyser.Type
import analyser.Types.UnitType
import scala.collection.mutable.Map
import analyser.SymTree
import analyser.Typed

abstract class Function(_name: String, _argsNames: List[String], _hasReturnValue: Boolean) extends BreedOwned with Typed{
    val name = _name
    val argsNames = _argsNames
    val hasReturnValue = _hasReturnValue
    val returnValue = AST.VariableValue("return") // Used to capture return type breed constraints
    val returnVariable = new Variable("return") // Used to capture return type constraints

    def getArguments():List[Variable]
}

/**
 * Code defined Function that does not belong to a breed yet
 */
case class UnlinkedFunction(_name: String, _argsNames: List[String], tokenBody: List[Token], _hasReturnValue: Boolean) extends Function(_name, _argsNames, _hasReturnValue){
    var body: AST = null
    override def toString(): String = {
        s"${body} (${argsNames}){${body}}"
    }
    val possibleBreed = Set[Breed]()

    def getArguments() = throw new Exception("Variable do not exists yet on an Unlinked Function")
}

/**
 * Code defined Function
 */
case class LinkedFunction(_name: String, _args: List[Variable], body: AST, breed: Breed, _hasReturnValue: Boolean) extends Function(_name, _args.map(_.name), _hasReturnValue){
    var symTree: SymTree = null
    
    override def getType():Type = {
        if (hasReturnValue){
            returnVariable.getType()
        }
        else{
            UnitType
        }
    }

    def getArguments() = _args
}

/**
 * Language predefined Function
 */ 
case class BaseFunction(_name: String, _args: List[Variable], _breed: Breed, returnType: Type, scalaCall: String) extends Function(_name, _args.map(_.name), returnType != UnitType){
    initConstraints(Set(_breed))

    returnVariable.setType(returnType,true)

    def getArguments() = _args

    def call(arg: List[String]): String = {
        var text = scalaCall
        _args.zip(arg).map{case (a, v) => {text = text.replace("$"+a.name, v)}}
        text
    }
}