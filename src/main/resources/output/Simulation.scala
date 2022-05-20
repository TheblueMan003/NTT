package example
package netlogo
import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import scala.collection.mutable
import meta.runtime.Actor

object Simulation extends App{
	val mainClass = MainInit.liftedMain
	val wolf : ClassWithObject[Wolf] = Wolf.reflect(IR)
	val observer : ClassWithObject[Observer] = Observer.reflect(IR)
	val agent : ClassWithObject[Agent] = Agent.reflect(IR)
	val turtle : ClassWithObject[Turtle] = Turtle.reflect(IR)
	val link : ClassWithObject[Link] = Link.reflect(IR)
	val patch : ClassWithObject[Patch] = Patch.reflect(IR)
	compileSims(List(turtle, observer, agent, wolf, link, patch), Some(mainClass))
}