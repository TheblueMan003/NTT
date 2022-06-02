package analyser

import netlogo.Breed
import netlogo.BreedOwned


trait BreedConstrainer{
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