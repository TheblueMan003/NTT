import parsing.Lexer
import parsing.StringBufferedIterator
import parsing.TokenBuffer
import parsing.Parser
import utils.Context

object Main extends App {
  val r1 = Lexer.parse(new StringBufferedIterator("globals [ test fuck ]"), new TokenBuffer())
  println(r1.list)
  val con = new Context()
  val r2 = Parser.functionDiscovery(r1.toBuffer(), con)
  println(con.variable)
}