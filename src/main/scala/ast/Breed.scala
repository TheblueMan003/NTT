package ast

import scala.collection.mutable.Map
import analyser.SymTree
import ast.LinkedFunction
import codegen.Renamer

class Breed(val parent: Breed, val singularName: String, val pluralName: String) extends VariableOwner{

    val ownedFuns: Map[String, Function] = Map[String, Function]()

    private var lambdaCounter = -1

    def className = Renamer.toClassName(singularName)

    override def addVariable(name: String) = {
        val vari = new Variable(name)
        ownedVars.addOne((name, vari))
        vari
    }
    override def addVariable(vari: Variable) = {
        ownedVars.addOne((vari.name, vari))
        vari
    }
    override def hasVariable(name: String): Boolean = {
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
    override def getVariable(name: String): Variable = {
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

    def addFunction(fun: Function):Unit = {
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
    def getAllFunctions() = ownedFuns.values

    def addLambda(tree: AST, parentCall: List[Variable]): LinkedFunction = {
        lambdaCounter += 1
        val name = f"lambda_${lambdaCounter}"
        val func = LinkedFunction(name, parentCall, tree, this, false)
        addFunction(func)
        func
    }
}

object Breed{
    case class TurtleBreed(_singularName: String, _pluralName: String, _parent: Breed) extends Breed(_parent, _singularName, _pluralName)
    case class LinkBreed(_singularName: String, _pluralName: String, directed: Boolean, _parent: Breed) extends Breed(_parent, _singularName, _pluralName)
    case class PatchBreed(_singularName: String, _pluralName: String, _parent: Breed) extends Breed(_parent, _singularName, _pluralName)
    case class ObserverBreed(_parent: Breed) extends Breed(_parent, "observer", "observers")
    case class AgentBreed() extends Breed(null, "agent", "agents")
}

trait BreedClass
object BreedClass{
    case class TurtleBreed() extends BreedClass
    case class LinkBreed(directed: Boolean) extends BreedClass
    case class PatchBreed() extends BreedClass
    case class Observer() extends BreedClass
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
    def restrainBreedTo(constraints: Set[Breed]):Boolean={
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
    def restrainBreedTo(constraints: BreedOwned):Boolean={
        val newb = breeds.intersect(constraints.breeds)
        val ret = newb != breeds
        breeds = newb
        ret
    }

    /**
     * Return if the object can be specialized for the set
     */ 
    def canBreedBeRestrainTo(constraints: Set[Breed]): Boolean = {
        !(breeds.intersect(constraints).isEmpty)
    }

    /**
     * Return if the object can be specialized for the set
     */ 
    def canBreedBeRestrainTo(constraints: BreedOwned): Boolean = {
        !(breeds.intersect(constraints.breeds).isEmpty)
    }

    /**
     * Force the function to belong to the highest breed(s) in the breed AST.
     * For Instance if the function belong to Turtle and something that inherite turtle. It will only belong to turtle
     */ 
    def removeDuplicatedBreed():Unit = {
        breeds = breeds.filter(breed => !breeds.contains(breed.parent))
    }
}