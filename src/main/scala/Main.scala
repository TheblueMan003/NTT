import parsing.Lexer
import utils._
import ast.{CompiledFunction, BaseFunction}
import parsing.Parser
import scala.io.Source

object Main extends App {
  val text = Source.fromResource("demo/example1.nlogo").getLines.reduce((x,y) => x + "\n" +y)
  val r1 = Lexer.parse(new StringBufferedIterator(text), new TokenBufferBuilder())
  println(r1.list)

  println

  val r2 = Parser.parse(r1.toIterator())
  println(r2.functions.map(f => 
    f._2 match {
      case c: CompiledFunction =>"\n" + c.name + "(" + c.argsNames+")->" + c.body+"\n"
      case c: BaseFunction =>"\n" + c.name + "(" + c.argsNames+")\n"
    }
  ))
  
}