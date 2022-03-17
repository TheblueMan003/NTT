import parsing.Lexer
import utils._
import parsing.Parser
import scala.io.Source

object Main extends App {
  val text = Source.fromResource("example1.logo").getLines.reduce((x,y) => x + "\n" +y)
  val r1 = Lexer.parse(new StringBufferedIterator(text), new TokenBufferBuilder())
  println(r1.list)

  println

  val r2 = Parser.parse(r1.toIterator())
  println(r2.functions.map(f => "\n" + f._1 + "->" + f._2.body+"\n"))
}