package utils

import ast.AST._
import scala.collection.mutable.Map
import scala.collection.mutable.Stack
import parsing.Token
import javax.swing.text.StyledEditorKit.BoldAction
import ast.AST
import ast.Variable
import ast.Breed
import ast.BreedType
import ast.{ Function, CompiledFunction }
import scala.collection.mutable.ListBuffer

trait VariableOwner{
}

// TODO Rename to a more fitting name
class Context(){
    val functions = Map[String, Function]()
    val breedsPlur = Map[String, Breed]()
    val breedsSing = Map[String, Breed]()

    val _agent =  new Breed.AgentBreed()
    val _observer =  new Breed.ObserverBreed(_agent)
    var _turtles = new Breed.TurtleBreed("turtle", "turtles", _agent)
    var _patches = new Breed.PatchBreed("patch", "patches", _agent)
    var _links = new Breed.LinkBreed("link", "linkes", true, _agent)


    addBreed(_agent)
    addBreed(_observer)
    addBreed(_turtles)
    addBreed(_patches)
    addBreed(_links)


    val _ownedBuffer = new ListBuffer[(String, String)]()


    def addFunction(name: String, args: List[String], body: List[Token], hasReturnValue: Boolean) = {
        functions.addOne((name, new CompiledFunction(name, args, body, hasReturnValue)))
    }
    def hasFunction(name: String): Boolean = functions.contains(name)
    def getFunction(name: String): Function = functions.get(name).get

    /** 
     * Add a bread of Type typ
     */ 
    def addBreed(singular: String, plural: String, typ: BreedType) = {
        if (breedsPlur.contains(plural)){
            throw new Exception(f"Duplicated bread: ${singular}, ${plural}")
        }
        typ match{
            case BreedType.TurtleBreed() => {
                val breed = new Breed.TurtleBreed(singular, plural, _turtles)
                breedsPlur.addOne((plural, breed))
                breedsSing.addOne((singular, breed))
            }
            case BreedType.LinkBreed(directed) => {
                val breed = new Breed.LinkBreed(singular, plural, directed, _links)
                breedsPlur.addOne((plural, breed))
                breedsSing.addOne((singular, breed))
            }
        }
    }
    // TODO Add Base Variable
    def addBreed(breed: Breed) = {
        breed match{
            case Breed.TurtleBreed(s, p, _) => {
                breedsPlur.addOne((p, breed))
                breedsSing.addOne((s, breed))
                functions.addAll(FunctionLoader.getAll(p, breed))
            }
            case Breed.LinkBreed(s, p, _, _) => {
                breedsPlur.addOne((p, breed))
                breedsSing.addOne((s, breed))
                functions.addAll(FunctionLoader.getAll(p, breed))
            }
            case Breed.PatchBreed(s, p, _) => {
                breedsPlur.addOne((p, breed))
                breedsSing.addOne((s, breed))
                functions.addAll(FunctionLoader.getAll(p, breed))
            }
            case Breed.ObserverBreed(_) => {
                breedsPlur.addOne(("observer", breed))
                breedsSing.addOne(("observer", breed))
                functions.addAll(FunctionLoader.getAll("observer", breed))
            }
            case Breed.AgentBreed() => {
                breedsPlur.addOne(("agent", breed))
                breedsSing.addOne(("agent", breed))
                functions.addAll(FunctionLoader.getAll("agents", breed))
            }
        }
    }
    def hasBreedPlural(name: String): Boolean = {
        breedsPlur.contains(name)
    }
    def hasBreedSingular(name: String): Boolean = {
        breedsSing.contains(name)
    }
    def getBreedPlural(name: String): Breed = {
        if (breedsPlur.contains(name)){
            breedsPlur.get(name).get
        }
        else{
            throw new Exception(f"Unknown breed ${name}")
        }
    }
    def getBreedSingular(name: String): Breed = {
        if (breedsSing.contains(name)){
            breedsSing.get(name).get
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
        breedsPlur.map(_._2).filter(_.hasVariable(name)).toSet
    }

    /**
      * @return Set of all breed
      */
    def getBreeds(): Set[Breed] = {
        breedsPlur.values.toSet
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
    def breedVariableOwnSetup() = {
        _ownedBuffer.map{ case (k, v) => {
                if (hasBreedPlural(k)){
                    getBreedPlural(k).addVariable(v)
                } else {
                    throw new Exception(f"Unknown Breed: ${k}")
                }
            }
        }

        // Add Base functions to child
        getBreeds().map(b => {
            val parents = listAllBreedParents(b)
            functions.values.filter(_.breeds.exists(parents.contains(_))).map{ f=> 
                f.extendTo(Set(b))
            }
        }
        )
    }

    /**
     * List all parent of a breed. Breed itself not included.
     */ 
    def listAllBreedParents(breed: Breed):List[Breed] = {
        if (breed.parent != null){
            breed.parent :: listAllBreedParents(breed.parent)
        }
        else{
            Nil
        }
    }


    /**
     * Add a variable with for the Variable Owner
     */ 
    def addVariable(name: String) = {
        ???
    }
    def hasVariable(name: String): Boolean = {
        breedsPlur.values.exists(_.hasVariable(name))
    }
    def getVariable(name: String): Variable = {
        breedsPlur.values
                .filter(_.hasVariable(name))
                .map(_.getVariable(name))
                .head
    }
}