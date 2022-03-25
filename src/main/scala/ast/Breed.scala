package ast

import scala.collection.mutable.Map

class Breed(_parent: Breed){
    val parent = _parent
    val ownedVars: Map[String, Variable] = Map[String, Variable]()
    val ownedFuns: Map[String, Function] = Map[String, Function]()

    def addVariable(name: String) = {
        ownedVars.addOne((name, Variable(name)))
    }
    def hasVariable(name: String): Boolean = {
        if (ownedVars.contains(name)){
            true
        }
        else if (parent != null){
            parent.hasVariable(name)
        }
        else{
            false
        }
    }
    def getVariable(name: String): Variable = {
        if (ownedVars.contains(name)){
            ownedVars.get(name).get
        }
        else if (parent != null){
            parent.getVariable(name)
        }
        else{
            throw new Exception(f"Unknown Variable: ${name}")
        }
    }


    def addFunction(fun: Function) = {
        ownedFuns.addOne((fun.name, fun))
    }
    def hasFunction(name: String): Boolean = {
        if (ownedFuns.contains(name)){
            true
        }
        else if (parent != null){
            parent.hasFunction(name)
        }
        else{
            false
        }
    }
    def getFunction(name: String): Function = {
        if (ownedFuns.contains(name)){
            ownedFuns.get(name).get
        }
        else if (parent != null){
            parent.getFunction(name)
        }
        else{
            throw new Exception(f"Unknown Function: ${name}")
        }
    }
}

object Breed{
    case class TurtleBreed(singularName: String, pluralName: String, _parent: Breed) extends Breed(_parent)
    case class LinkBreed(singularName: String, pluralName: String, directed: Boolean, _parent: Breed) extends Breed(_parent)
    case class PatchBreed(singularName: String, pluralName: String, _parent: Breed) extends Breed(_parent)
    case class ObserverBreed(_parent: Breed) extends Breed(_parent)
    case class AgentBreed() extends Breed(null)
}

trait BreedType
object BreedType{
    case class TurtleBreed() extends BreedType
    case class LinkBreed(directed: Boolean) extends BreedType
    case class PatchBreed() extends BreedType
    case class Observer() extends BreedType
}

trait BreedOwned{
    var breeds = Set[Breed]()
    
    def initConstraints(constraints: Set[Breed])={
        breeds = constraints
    }

    /**
     * Restraint the object to the breeds
     * 
     * @return true if breed set changed
     */
    def extendTo(constraints: Set[Breed]):Boolean={
        val newb = breeds.union(constraints)
        val ret = newb != breeds
        breeds = newb
        ret
    }

    /**
     * Restraint the object to the breeds
     * 
     * @return true if breed set changed
     */
    def restrainTo(constraints: Set[Breed]):Boolean={
        val newb = breeds.intersect(constraints)
        val ret = newb != breeds
        breeds = newb
        ret
    }

    /**
     * Restraint the object to the breeds
     * 
     * @return true if breed set changed
     */
    def restrainTo(constraints: BreedOwned)={
        val newb = breeds.intersect(constraints.breeds)
        val ret = newb != breeds
        breeds = newb
        ret
    }

    /**
     * Return if the object can be specialized for the set
     */ 
    def canBeRestrainTo(constraints: Set[Breed]): Boolean = {
        !(breeds.intersect(constraints).isEmpty)
    }

    /**
     * Return if the object can be specialized for the set
     */ 
    def canBeRestrainTo(constraints: BreedOwned): Boolean = {
        !(breeds.intersect(constraints.breeds).isEmpty)
    }
}