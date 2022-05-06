
object Simulation extends App{
	val mainClass = MainInit.liftedMain
	val observer : ClassWithObject[Observer] = Observer.reflect(IR)
	val agent : ClassWithObject[Agent] = Agent.reflect(IR)
	val turtle : ClassWithObject[Turtle] = Turtle.reflect(IR)
	val link : ClassWithObject[Link] = Link.reflect(IR)
	val patch : ClassWithObject[Patch] = Patch.reflect(IR)
	compileSims(List(turtle, observer, agent, link, patch), Some(mainClass))
}