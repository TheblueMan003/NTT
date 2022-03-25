import parsing.Lexer
import utils._
import ast.{CompiledFunction, BaseFunction}
import parsing.Parser
import analyser.BreedAnalyser
import scala.io.Source

object Main extends App {
  val text = Source.fromResource("demo/example1.nlogo").getLines.reduce((x,y) => x + "\n" +y)
  val r1 = Lexer.tokenize(new StringBufferedIterator(text), new TokenBufferBuilder())
  println(r1.list)

  println

  val context = Parser.parse(r1.toIterator())
  BreedAnalyser.analyse(context)

  println(context.functions.map(f => 
    f._2 match {
      case c: CompiledFunction => f"\n${c.name}[${c.breeds}](${c.argsNames})->${c.body}\n"
      case c: BaseFunction =>""
    }
  ))
}