import parsing.Lexer
import utils._
import netlogo.{LinkedFunction, BaseFunction}
import parsing.Parser
import analyser.{BreedAnalyser, NameAnalyser, TypeChecker}
import scala.io.Source
import codegen.CodeGen
import scala.io.StdIn.readLine
import netlogo.UnlinkedFunction
import codegen.Exporter

object Main extends App {
  Reporter.debugEnabled = true

  val filenames = if (args.length == 0) {
    var file = readLine()
    if (file.trim == "") file = "example1"
    List(f"demo/${file}.nlogo")
  }
  else {
    args.toList
  }

  var files = List("base_breeds/logo/std.nlogo") ::: filenames

  val tokens = files.map(FileLoader.load(_))
                    .map(Lexer.tokenize(_)).foldLeft(new TokenBufferBuilder())(_.union(_))
  Reporter.info("Tokenization complete")
  val context = Parser.parse(tokens.toIterator())
  Reporter.info("Parsing complete")

  // Analysis
  BreedAnalyser.analyse(context)
  Reporter.info("Breed analysis complete")

  NameAnalyser.analyse(context)
  Reporter.info("Name analysis complete")

  TypeChecker.analyse(context)
  Reporter.info("Type analysis complete")
  
  val code = CodeGen.generate(context)
  Reporter.info("Code generation complete")

  Exporter.export(code, "./runner/src")
  Reporter.info("Code written to file")
}