 import org.scalatest.funsuite.AnyFunSuite
import utils.StringBufferedIterator
import parsing.Lexer
import parsing.Tokens
    import parsing.Parser
    import utils.Context
    import analyser.SymTree
    import ast.AST
    import utils.TokenBufferedIterator
    
 class ParserTest extends AnyFunSuite {
    test("Expression With Operator order") {
        val buff = new TokenBufferedIterator(List(
            Tokens.IntLitToken(1),
            Tokens.OperatorToken("*"),
            Tokens.IntLitToken(2),
            Tokens.OperatorToken("+"),
            Tokens.IntLitToken(3)
        ))
        val context = new Context()
        val res = Parser.parseExpression()(buff, context)
        assert(res === AST.BinarayExpr("+", AST.BinarayExpr("*", AST.IntValue(1), AST.IntValue(2)), AST.IntValue(3)))
    }
    test("Expression With Operator order 2") {
        val buff = new TokenBufferedIterator(List(
            Tokens.IntLitToken(1),
            Tokens.OperatorToken("+"),
            Tokens.IntLitToken(2),
            Tokens.OperatorToken("*"),
            Tokens.IntLitToken(3)
        ))
        val context = new Context()
        val res = Parser.parseExpression()(buff, context)
        assert(res === AST.BinarayExpr("+",  AST.IntValue(1), AST.BinarayExpr("*", AST.IntValue(2), AST.IntValue(3))))
    }
    test("If Empty Block") {
        val buff = new TokenBufferedIterator(List(
            Tokens.KeywordToken("if"),
            Tokens.BoolLitToken(true),
            Tokens.DelimiterToken("["),
            Tokens.DelimiterToken("]")
        ))
        val context = new Context()
        val res = Parser.parseIntruction()(buff, context)
        assert(res === AST.IfBlock(AST.BooleanValue(true), AST.Block(List())))
    }
    test("If Esle Empty Block") {
        val buff = new TokenBufferedIterator(List(
            Tokens.KeywordToken("ifelse"),
            Tokens.BoolLitToken(true),
            Tokens.DelimiterToken("["),
            Tokens.DelimiterToken("]"),
            Tokens.DelimiterToken("["),
            Tokens.DelimiterToken("]")
        ))
        val context = new Context()
        val res = Parser.parseIntruction()(buff, context)
        assert(res === AST.IfElseBlock(List((AST.BooleanValue(true), AST.Block(List()))), AST.Block(List())))
    }
    test("Repeat") {
        val buff = new TokenBufferedIterator(List(
            Tokens.KeywordToken("repeat"),
            Tokens.IntLitToken(1),
            Tokens.DelimiterToken("["),
            Tokens.DelimiterToken("]")
        ))
        val context = new Context()
        val res = Parser.parseIntruction()(buff, context)
        assert(res === AST.Repeat(AST.IntValue(1),AST.Block(List())))
    }
    test("While") {
        val buff = new TokenBufferedIterator(List(
            Tokens.KeywordToken("while"),
            Tokens.BoolLitToken(true),
            Tokens.DelimiterToken("["),
            Tokens.DelimiterToken("]")
        ))
        val context = new Context()
        val res = Parser.parseIntruction()(buff, context)
        assert(res === AST.While(AST.BooleanValue(true),AST.Block(List())))
    }
    test("Loop") {
        val buff = new TokenBufferedIterator(List(
            Tokens.KeywordToken("loop"),
            Tokens.DelimiterToken("["),
            Tokens.DelimiterToken("]")
        ))
        val context = new Context()
        val res = Parser.parseIntruction()(buff, context)
        assert(res === AST.Loop(AST.Block(List())))
    }
 }