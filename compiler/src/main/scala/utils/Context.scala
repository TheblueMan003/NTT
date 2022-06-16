package utils

import ast.AST._
import scala.collection.mutable.Map
import scala.collection.mutable.Stack
import parsing.Token
import javax.swing.text.StyledEditorKit.BoldAction
import ast.AST
import netlogo.Variable
import netlogo.Breed
import netlogo.BreedClass
import netlogo.{ Function, UnlinkedFunction }
import scala.collection.mutable.ListBuffer

trait VariableOwner{
}

class Context(){
    val functions = Map[String, Function]()
    val breedsPlur = Map[String, Breed]()
    val breedsSing = Map[String, Breed]()

    val _agent =  new Breed.AgentBreed()
    val _observer =  new Breed.ObserverBreed(_agent)
    var _turtles = new Breed.TurtleBreed("turtle", "turtles", _agent)
    var _patches = new Breed.PatchBreed("patch", "patches", _agent)
    var _links = new Breed.LinkBreed("link", "linkes", true, _agent)

    // Add Default NetLogo Breeds
    addBreed(_agent)
    addBreed(_observer)
    addBreed(_turtles)
    addBreed(_patches)
    addBreed(_links)


    val _ownedBuffer = new ListBuffer[(String, String)]()

    /**
      * Add function to the context
      *
      * @param name: name of the function
      * @param args: list of arguments of the function
      * @param body: body of the function
      * @param hasReturnValue: true if the function has a return value
      */
    def addFunction(name: String, args: List[String], body: List[Token], hasReturnValue: Boolean) = {
        functions.addOne((name, new UnlinkedFunction(name, args, body, hasReturnValue)))
    }

    /**
      * @param name: The name of the function
      * @return true if the function exists, false otherwise
      */
    def hasFunction(name: String): Boolean = functions.contains(name)

    /**
      * return the function with the given name
      *
      * @param name: the name of the function
      * @return the function with the given name
      */
    def getFunction(name: String): Function = functions.get(name).get
    
    /**
      * Return the set of breed that contains the function name
      *
      * @param name: Name of the function needed
      * @return breed that contains the function
      */
    def getBreedsWithFunction(name: String): Set[Breed] = {
        breedsPlur.map(_._2).filter(_.hasFunction(name)).toSet
    }

  /**
    * Add a breed to the context
    *
    * @param singular: Singular name of the breed
    * @param plural: Plural name of the breed
    * @param typ: Type of the breed
    */
    def addBreed(singular: String, plural: String, typ: BreedClass) = {
        if (breedsPlur.contains(plural)){
            throw new Exception(f"Duplicated bread: ${singular}, ${plural}")
        }
        typ match{
            case BreedClass.TurtleBreed() => {
                val breed = new Breed.TurtleBreed(singular, plural, _turtles)
                breedsPlur.addOne((plural, breed))
                breedsSing.addOne((singular, breed))
            }
            case BreedClass.LinkBreed(directed) => {
                val breed = new Breed.LinkBreed(singular, plural, directed, _links)
                breedsPlur.addOne((plural, breed))
                breedsSing.addOne((singular, breed))
            }
        }
    }

    /**
      * Add a breed to the context
      *
      * @param breed: Breed to add
      */
    def addBreed(breed: Breed) = {
        breed match{
            case Breed.TurtleBreed(s, p, _) => {
                breedsPlur.addOne((p, breed))
                breedsSing.addOne((s, breed))
            }
            case Breed.LinkBreed(s, p, _, _) => {
                breedsPlur.addOne((p, breed))
                breedsSing.addOne((s, breed))
            }
            case Breed.PatchBreed(s, p, _) => {
                breedsPlur.addOne((p, breed))
                breedsSing.addOne((s, breed))
            }
            case Breed.ObserverBreed(_) => {
                breedsPlur.addOne(("observer", breed))
                breedsSing.addOne(("observer", breed))
            }
            case Breed.AgentBreed() => {
                breedsPlur.addOne(("agents", breed))
                breedsSing.addOne(("agent", breed))
            }
        }
    }

    /**
      * Return true the breed with the name exists
      *
      * @param name: of the breed (plural)
      * @return Boolean
      */
    def hasBreedPlural(name: String): Boolean = {
        breedsPlur.contains(name)
    }

    /**
      * Return true the breed with the name exists
      *
      * @param name: of the breed (singular)
      * @return Boolean
      */
    def hasBreedSingular(name: String): Boolean = {
        breedsSing.contains(name)
    }

    /**
      * Return the breed with the name
      *
      * @param name: of the breed (plural)
      * @return Breed
      */
    def getBreedPlural(name: String): Breed = {
        if (breedsPlur.contains(name)){
            breedsPlur.get(name).get
        }
        else{
            throw new Exception(f"Unknown breed ${name}")
        }
    }

    /**
      * Return the breed with the name
      *
      * @param name: of the breed (singular)
      * @return Breed
      */
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
      * @return Return the agent Breed
      */
    def getAgentBreed(): Breed = {
        _agent
    }

    /**
      * @return Return the turtle Breed
      */
    def getTurtleBreed(): Breed = {
        _turtles
    }

    /**
      * @return Return the link Breed
      */
    def getLinkBreed(): Breed = {
        _links
    }

    /**
      * @return Return the patch Breed
      */
    def getPatchBreed(): Breed = {
        _patches
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
                    val vari = getBreedPlural(k).addVariable(v, true)
                    if (getBreedPlural(k) == getObserverBreed()){
                        vari.isGlobal = true
                    }
                } else {
                    throw new Exception(f"Unknown Breed: ${k}")
                }
            }
        }

        getBreeds().map(b => functions.addAll(FunctionLoader.getAll(b.pluralName, b)(this)))

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
     * @param name: of the variable
     * @return true if the variable with name exists
     */
    def hasVariable(name: String): Boolean = {
        breedsPlur.values.exists(_.hasVariable(name))
    }

    /**
      * Return the variable with the name
      * 
      * @param name: of the variable
      * @return Variable
      */
    def getVariable(name: String): Variable = {
        breedsPlur.values
                .filter(_.hasVariable(name))
                .map(_.getVariable(name))
                .head
    }
}