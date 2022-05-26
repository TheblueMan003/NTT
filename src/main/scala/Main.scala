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

  Reporter.debug(tokens.list.toString())

  val context = Parser.parse(tokens.toIterator())
  BreedAnalyser.analyse(context)
  NameAnalyser.analyse(context)
/*
  Reporter.debug(context.getBreeds().map(b => b.getAllFunctions().filter(f =>
    f match {
      case c: LinkedFunction => true
      case _ => false
    }).map(f => 
    f match {
      case c: LinkedFunction => println(f"${c.name} -> ${c.symTree}\n\n")
    }
  )))*/

  TypeChecker.analyse(context)
  
  val code = CodeGen.generate(context)
  code.map(c => c.writeToFile(f"./src/main/resources/output/${c.name}.scala"))
/*
  Reporter.debug(context.getBreeds().map(b => b.getAllFunctions().filter(f =>
    f match {
      case c: LinkedFunction => true
      case _ => false
    }).map(f => 
    f match {
      case c: LinkedFunction => f"${b} -> ${c.name}\n"
    }
  )))

  Reporter.debug(context.getBreeds().flatMap(_.getAllFunctions().map(f => 
    f match {
      case c: LinkedFunction => f"\n\n${c.name}[${c.breeds}](${c.argsNames})->${c.symTree}"
      case c: BaseFunction => ""
    }
  )))

  Reporter.debug(context.getBreeds().flatMap(_.getAllVariables().map(v => 
    f"\n${v.name}: ${v.getType()}"
  )))
  
  Reporter.debug(context.getBreeds().flatMap(_.getAllFunctions()
  .filter(
    _ match {
      case l: LinkedFunction => true
      case _ => false
    }
  )
  .map(f => f.getArguments().map(v => 
    f"${f.name} ${v.name}: ${v.getType()}\n"
  ))))*/
}