import parsing.Lexer
import utils._
import netlogo.{LinkedFunction, BaseFunction}
import parsing.Parser
import analyser.{BreedAnalyser, NameAnalyser, TypeChecker}
import scala.io.Source
import codegen.CodeGen

object Main extends App {
  Reporter.debugEnabled = true

  var files = List("base_breeds/logo/std.nlogo", "demo/example1.nlogo")

  val tokens = files.map(FileLoader.load(_))
                    .map(Lexer.tokenize(_)).foldLeft(new TokenBufferBuilder())(_.union(_))

  val context = Parser.parse(tokens.toIterator())

  // Analysis
  BreedAnalyser.analyse(context)
  NameAnalyser.analyse(context)

  TypeChecker.analyse(context)
  
  val code = CodeGen.generate(context)

  code.map(c => c.writeToFile(f"./runner/src/${c.name}.scala"))
}