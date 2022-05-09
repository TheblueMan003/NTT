import org.scalatest.funsuite.AnyFunSuite
import utils.StringBufferedIterator
import parsing.Lexer
import parsing.Tokens
import parsing.Parser
import analyser.BreedAnalyser
import utils.Context
import analyser.SymTree
import ast.AST
import utils.TokenBufferedIterator
import scala.io.Source

class BreedTest extends AnyFunSuite {
    test("Using breed own force function breed") {
        val text =  Source.fromURL(getClass.getResource("/Analyser/Breeds/varTest.nlogo")).getLines.reduce((x,y) => x + "\n" +y) + "\n"
        val buffer = new StringBufferedIterator(text, "example")
        val tokens = Lexer.tokenize(buffer)
        val context = Parser.parse(tokens.toIterator())
        BreedAnalyser.analyse(context)

        assert(context.getBreedSingular("wolf").hasFunction("fct") === true)
    }
    test("Function call for other force function breed") {
        val text =  Source.fromURL(getClass.getResource("/Analyser/Breeds/recTest.nlogo")).getLines.reduce((x,y) => x + "\n" +y) + "\n"
        val buffer = new StringBufferedIterator(text, "example")
        val tokens = Lexer.tokenize(buffer)
        val context = Parser.parse(tokens.toIterator())
        BreedAnalyser.analyse(context)

        assert(context.getBreedSingular("wolf").hasFunction("fct") === true)
    }
}