import org.scalatest.funsuite.AnyFunSuite
import utils.StringBufferedIterator
import parsing.Lexer
import parsing.Tokens
    
 class LexerTest extends AnyFunSuite {
    test("IntLitToken") {
        val text = "0"
        val buff = new StringBufferedIterator(text, "test")
        val tokens = Lexer.tokenize(buff).toIterator()
        assert(tokens.take() === Tokens.IntLitToken(0))
    }
    test("FloatLitToken") {
        val text = "0.0"
        val buff = new StringBufferedIterator(text, "test")
        val tokens = Lexer.tokenize(buff).toIterator()
        assert(tokens.take() === Tokens.FloatLitToken(0.0f))
    }
    test("StringLitToken") {
        val text = "\"test\""
        val buff = new StringBufferedIterator(text, "test")
        val tokens = Lexer.tokenize(buff).toIterator()
        assert(tokens.take() === Tokens.StringLitToken("test"))
    }
    test("BooleanLitToken") {
        val text = "true false"
        val buff = new StringBufferedIterator(text, "test")
        val tokens = Lexer.tokenize(buff).toIterator()
        assert(tokens.take() === Tokens.BoolLitToken(true))
        assert(tokens.take() === Tokens.BoolLitToken(false))
    }
    test("OperatorToken") {
      Lexer.operators.map(text => {
        val buff = new StringBufferedIterator(text, "test")
        val tokens = Lexer.tokenize(buff).toIterator()
        assert(tokens.take() === Tokens.OperatorToken(text))
        text
      }
      )
   }
   test("DelimiterToken") {
      Lexer.delimiter.map(char => {
        val text = char.toString()
        val buff = new StringBufferedIterator(text, "test")
        val tokens = Lexer.tokenize(buff).toIterator()
        assert(tokens.take() === Tokens.DelimiterToken(text))
        text
      }
      )
   }
   test("KeywordToken") {
      Lexer.keyword.map(text => {
        val buff = new StringBufferedIterator(text, "test")
        val tokens = Lexer.tokenize(buff).toIterator()
        assert(tokens.take() === Tokens.KeywordToken(text))
        text
      }
      )
   }
   test("IdentifierToken") {
        val text = "test"
        val buff = new StringBufferedIterator(text, "test")
        val tokens = Lexer.tokenize(buff).toIterator()
        assert(tokens.take() === Tokens.IdentifierToken("test"))
    }
    test("Remove Space") {
        val text = "     "
        val buff = new StringBufferedIterator(text, "test")
        val tokens = Lexer.tokenize(buff).toIterator()
        assert(tokens.hasNext() === false)
    }
    test("Remove Comment") {
        val text = ";; test"
        val buff = new StringBufferedIterator(text, "test")
        val tokens = Lexer.tokenize(buff).toIterator()
        assert(tokens.hasNext() === false)
    }
 }