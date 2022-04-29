
object Simulation extends App{
	val mainClass = MainInit.liftedMain
	val turtle : ClassWithObject[Turtle] = Reader.reflect(IR)
	val patch : ClassWithObject[Patch] = Reader.reflect(IR)
	val observer : ClassWithObject[Observer] = Reader.reflect(IR)
	val agent : ClassWithObject[Agent] = Reader.reflect(IR)
	val link : ClassWithObject[Link] = Reader.reflect(IR)
	val wolf : ClassWithObject[Wolf] = Reader.reflect(IR)
	compileSims(List(Patch, Agent, Turtle, Link, Observer, Wolf), Some(mainClass))
}