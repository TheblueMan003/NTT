import parsing.Lexer
import parsing.StringBufferedIterator
import parsing.TokenBuffer
import parsing.Parser
import utils.Context
import scala.io.Source

object Main extends App {
  val text = Source.fromResource("example1.logo").getLines.reduce((x,y) => x + "\n" +y)
  val r1 = Lexer.parse(new StringBufferedIterator(text), new TokenBuffer())
  println(r1.list)
  val con = new Context()
  val r2 = Parser.functionDiscovery()(r1.toIterator(), con)
  println(con.functions)
}