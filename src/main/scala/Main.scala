import parsing.Lexer
import utils._
import ast.{LinkedFunction, BaseFunction}
import parsing.Parser
import analyser.{BreedAnalyser, NameAnalyser}
import scala.io.Source

object Main extends App {
  Reporter.debugEnabled = true

  val text = Source.fromResource("demo/example1.nlogo").getLines.reduce((x,y) => x + "\n" +y)
  val buffer = new StringBufferedIterator(text, "example")
  val tokens = Lexer.tokenize(buffer)
  Reporter.debug(tokens.list.toString())

  val context = Parser.parse(tokens.toIterator())
  BreedAnalyser.analyse(context)
  NameAnalyser.analyse(context)

  Reporter.debug(context.getBreeds().map(b => b.getAllFunctions().filter(f =>
    f match {
      case c: LinkedFunction => true
      case _ => false
    }).map(f => 
    f match {
      case c: LinkedFunction => f"${b} -> ${c.name}\n"
    }
  )))

  Reporter.debug(context.getBreeds().map(_.getAllFunctions().map(f => 
    f match {
      case c: LinkedFunction => f"\n${c.name}[${c.breeds}](${c.argsNames})->${c.symTree}\n"
      case c: BaseFunction => ""
    }
  )))
}