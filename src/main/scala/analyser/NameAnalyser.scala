package analyser

import utils.Context
import utils.CompiledFunction
import ast._

object NameAnalyser{
    def analyse(context: Context) = {
        initConstraints()(context)
    }
    def initConstraints()(implicit context: Context) = {
        context.functions.values.map(
            _ match {
                case cf: CompiledFunction => cf.initConstraints(context.getBreeds()) 
                case _ =>
            }
        )
    }
    def analyse(tree: Tree)(implicit context: Context): (Set[Breed], Tree) = {
        tree match{
            case Ask(block) => {
                 
            }
            case expr: Expression => (null, expr)
        }
    }

    /**
      * Return the intercect two breeds set
      *
      * @param set1
      * @param set2
      * @return
      */
    def mergeBreedConstraints(set1: Set[Breed], set2: Set[Breed]):Set[Breed] = {
        if (set1 == null){
            set2
        }
        else if (set2 == null){
            set1
        }
        else{
            set1.intersect(set2)
        }
    }
    def duplicate(){

    }
}