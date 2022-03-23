package utils

import ast.Tree._
import scala.collection.mutable.Map
import scala.collection.mutable.Stack
import parsing.Token
import javax.swing.text.StyledEditorKit.BoldAction
import ast.Tree
import ast.Variable
import ast.Breed
import ast.BreedType
import ast.{ Function, CompiledFunction }
import scala.collection.mutable.ListBuffer

trait VariableOwner{
}

class Context(){
    val functions = Map[String, Function]()
    val breeds = Map[String, Breed]()

    val _targetBreeds: Stack[Set[Breed]] = Stack[Set[Breed]]()
    val _observer =  new Breed.Observer()
    var _turtles = new Breed.TurtleBreed("turtle", "turtles", _observer)
    var _patches = new Breed.PatchBreed("patch", "patches", _observer)
    var _links = new Breed.LinkBreed("link", "linkes", true, _observer)

    breeds.addOne(("$observer", _observer))
    breeds.addOne(("turtles", _turtles))
    breeds.addOne(("patches", _patches))
    breeds.addOne(("linkes", _links))

    functions.addAll(FunctionLoader.getAll("turtles"))

    val _ownedBuffer = new ListBuffer[(String, String)]()


    def addFunction(name: String, args: List[String], body: List[Token]) = {
        functions.addOne((name, new CompiledFunction(name, args, body)))
    }
    def hasFunction(name: String): Boolean = functions.contains(name)
    def getFunction(name: String): Function = functions.get(name).get

    /** 
     * Add a bread of Type typ
     */ 
    def addBreed(singular: String, plural: String, typ: BreedType) = {
        if (breeds.contains(plural)){
            throw new Exception(f"Duplicated bread: ${singular}, ${plural}")
        }
        typ match{
            case BreedType.TurtleBreed() => breeds.addOne((plural, new Breed.TurtleBreed(singular, plural, _turtles)))
            case BreedType.LinkBreed(directed) => breeds.addOne((plural, new Breed.LinkBreed(singular, plural, directed, _links)))
        }
    }
    def hasBreed(name: String): Boolean = {
        breeds.contains(name)
    }
    def getBreed(name: String): Breed = {
        if (breeds.contains(name)){
            breeds.get(name).get
        }
        else{
            throw new Exception(f"Unknown breed ${name}")
        }
    }
    /**
      * Return the set of breed that contains the variable name
      *
      * @param name: Name of the variable needed
      * @return breed that contains the variables
      */
    def getBreedsWithVariable(name: String): Set[Breed] = {
        breeds.map(_._2).filter(_.hasVariable(name)).toSet
    }

    /**
      * @return Set of all breed
      */
    def getBreeds(): Set[Breed] = {
        breeds.values.toSet
    }
    /**
      * @return Return the observer Breed
      */
    def getObserverBreed(): Breed = {
        _observer
    }

    /** 
     * Add variable vari to breed
     */ 
    def addOwned(breed: String, vari: String) = {
        _ownedBuffer.addOne((breed, vari))
    }
    /** 
     * Add all variables to their breed
     */ 
    def breadVariableOwnSetup() = {
        _ownedBuffer.map{ case (k, v)=> getBreed(k).addVariable(v) }
    }


    /**
     * Add a variable with for the Variable Owner
     */ 
    def addVariable(name: String) = {
        ???
    }
    def hasVariable(name: String): Boolean = {
        if (_targetBreeds.isEmpty){
            breeds.values.exists(_.hasVariable(name))
        }
        else{
            _targetBreeds.top.exists(_.hasVariable(name))
        }
    }
    def getVariable(name: String): Variable = {
        if (_targetBreeds.isEmpty){
            breeds.values
                .filter(_.hasVariable(name))
                .map(_.getVariable(name))
                .head
        }
        else{
            _targetBreeds
                .top
                .filter(_.hasVariable(name))
                .map(_.getVariable(name))
                .head
        }
    }
}