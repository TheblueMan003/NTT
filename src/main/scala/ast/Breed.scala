package ast

import scala.collection.mutable.Map

class Breed(parent: Breed){
    val owned: Map[String, Variable] = Map[String, Variable]()

    def addVariable(name: String) = {
        owned.addOne((name, Variable(name)))
    }
    def hasVariable(name: String): Boolean = {
        if (owned.contains(name)){
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
        if (owned.contains(name)){
            owned.get(name).get
        }
        else if (parent != null){
            parent.getVariable(name)
        }
        else{
            throw new Exception(f"Unknown Variable: ${name}")
        }
    }
}

object Breed{
    case class TurtleBreed(singularName: String, pluralName: String, parent: Breed) extends Breed(parent)
    case class LinkBreed(singularName: String, pluralName: String, directed: Boolean, parent: Breed) extends Breed(parent)
    case class PatchBreed(singularName: String, pluralName: String, parent: Breed) extends Breed(parent)
    case class Observer() extends Breed(null)
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