import parsing.Lexer
import parsing.StringBufferedIterator
import parsing.TokenBuffer

object Main extends App {
  val r = Lexer.parse(new StringBufferedIterator("global [ test ]"), new TokenBuffer())
  println(r.list)
}