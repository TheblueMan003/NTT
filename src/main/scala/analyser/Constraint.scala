package analyser

import ast.Breed
import ast.BreedOwned

abstract class Type{
}
object Types{
    case class IntType() extends Type
    case class FloatType() extends Type
    case class BoolType() extends Type
    case class StringType() extends Type
    case class TurtleType(breed: Breed) extends Type


    case class VariableType(id: Int) extends Type
}

abstract class Constraint{
}
object Constraints{
    case class TypeConstraint(found: Type, expected: Type) extends Constraint
}



abstract class BreedConstrainer{
    def getBreeds(): Set[Breed]
}
object BreedConstrainer{
    case class BreedSet(set: Set[Breed]) extends BreedConstrainer{
        override def getBreeds() = set
    }
    case class BreedOwn(breedOwned: BreedOwned) extends BreedConstrainer{
        override def getBreeds() = breedOwned.breeds
    }
}
case class BreedConstraint(found: BreedConstrainer, expected: BreedConstrainer)

case class BreedException(found: BreedConstrainer, expect: BreedConstrainer) extends Exception{
    override def getMessage():String = {
        return f"Expected: ${expect.getBreeds()} Found: ${found.getBreeds()}"
    }
}