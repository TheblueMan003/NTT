import parsing.Lexer
import parsing.StringBufferedIterator
import parsing.TokenBuffer
import parsing.Parser
import utils.Context
import scala.io.Source

object Main extends App {
  val text = Source.fromResource("example1.nlogo").getLines.reduce((x,y) => x + "\n" +y)
  val r1 = Lexer.parse(new StringBufferedIterator(text), new TokenBuffer())
  println(r1.list)

  println

  val r2 = Parser.parse(r1.toIterator())
  println(r2.functions.map(f => "\n" + f._1 + "->" + f._2.body+"\n"))
}