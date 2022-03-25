package parsing

import ast.AST
import ast.AST.Expression
import Tokens._
import utils.Context
import ast.CompiledFunction
import utils.VariableOwner
import utils.TokenBufferedIterator
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer
import ast.BreedType._

object Parser{
    
    private var operatorsPriority = List(List("<", ">", "<=", ">=" ,"=","!="), List("^"), List("*", "/", "and", "mod"), List("+", "-", "or", "xor")).reverse

    def parse(text: TokenBufferedIterator): Context = {
        val con = new Context()

        // Phase 1 - Function & Breed Discovery
        functionDiscovery()(text, con)

        // Phase 1.5 - Add All owned variable to their breed
        con.breedVariableOwnSetup()

        // Phase 2 - Parse Inside of Function
        parseAllFuncitonsBody()(con)
        con
    }

    /**
     * Get All functions and variables from code 
     */
    def functionDiscovery()(implicit text: TokenBufferedIterator, context: Context): Unit = {
        val c = text.take()
        c match{
            case KeywordToken("to") => {
                val name = text.getIdentifier()
                val argName = getArgsName()
                val body = getFunctionBody()
            
                context.addFunction(name, argName, body)
            }

            case KeywordToken("to-report") => {
                val name = text.getIdentifier()
                val argName = getArgsName()
                val body = getFunctionBody()
            
                context.addFunction(name, argName, body)
            }

            case KeywordToken("breed") => {
                val names = getVariablesGroup()
                if (names.size != 2) throw new Exception("Wrong Number of names for breed")
                context.addBreed(names(0), names(1), TurtleBreed())
            }

            case KeywordToken("undirected-link-breed") => {
                val names = getVariablesGroup()
                if (names.size != 2) throw new Exception("Wrong Number of names for breed")
                context.addBreed(names(0), names(1), LinkBreed(false))
            }

            case KeywordToken("directed-link-breed") => {
                val names = getVariablesGroup()
                if (names.size != 2) throw new Exception("Wrong Number of names for breed")
                context.addBreed(names(0), names(1), LinkBreed(true))
            }

            case KeywordToken("globals") => {
                getVariablesGroup().map(
                    context.addOwned("$observer", _)
                )
            }

            case IdentifierToken(iden) => {
                if (iden.endsWith("-own")){
                    val breed = iden.dropRight(4)
                    getVariablesGroup().map(
                        context.addOwned(breed, _)
                    )
                }
                else{
                    throw new UnexpectedTokenException(c, IdentifierToken("_-own"))
                }
            }
        }
        if (text.hasNext()){
            functionDiscovery()
        }
    }
    
    def parseAllFuncitonsBody()(context: Context) = {
        context.functions.values.map( f =>
            f match {
                case cf: CompiledFunction => {
                    cf.body = parseFunctionsBody()(new TokenBufferedIterator(cf.tokenBody), context)
                }
                case _ => 
            }
        )
    }

    /**
     * Parse inside of Function Body
     */
    def parseFunctionsBody()(implicit text: TokenBufferedIterator, context: Context):AST = {
        val body = ListBuffer[AST]()
        while(text.hasNext()){
            body.addOne(parseIntruction())
        }
        return AST.Block(body.toList)
    }


    /**
     * Parse an instruction
     */ 
    def parseIntruction()(implicit text: TokenBufferedIterator, context: Context): AST = {
        if (text.isKeyword("let")){
            text.take()
            val iden = text.getIdentifier()
            val value = parseExpression()
            AST.Declaration(AST.VariableValue(iden), value)
        }
        else if (text.isKeyword("set")){
            text.take()
            val iden = text.getIdentifier()
            val value = parseExpression()
            AST.Assignment(AST.VariableValue(iden), value)
        }
        else if (text.isIdentifier()){
            val iden = text.getIdentifier()
            if (context.hasFunction(iden)){
                parseCall(iden)
            }
            else{
                throw new Exception(f"Unknown function: ${iden}")
            }
        }
        else if (text.isKeyword("if")){
            text.take()
            val cond = parseExpression()
            val cmds = parseInstructionBlock()

            AST.IfBlock(cond, cmds)
        }
        else if (text.isKeyword("ifelse")){
            text.take()
            val buffer = ListBuffer[(Expression, AST)]()

            while(!text.isDelimiter("[")){
                val cond = parseExpression()
                val cmds = parseInstructionBlock()
                buffer.addOne((cond, cmds))
            }

            val cmds = parseInstructionBlock()

            AST.IfElseBlock(buffer.toList, cmds)
        }
        else if (text.isKeyword("ifelse-value")){
            text.take()
            val buffer = ListBuffer[(Expression, Expression)]()

            while(!text.isDelimiter("[")){
                val cond = parseExpression()
                text.requierToken(DelimiterToken("["))
                val value = parseExpression()
                text.requierToken(DelimiterToken("]"))
                buffer.addOne((cond, value))
            }

            text.requierToken(DelimiterToken("["))
            val value = parseExpression()
            text.requierToken(DelimiterToken("]"))

            AST.IfElseBlockExpression(buffer.toList, value)
        }
        else if (text.isKeyword("loop")){
            text.take()
            val cmds = parseInstructionBlock()
            AST.Loop(cmds)
        }
        else if (text.isKeyword("repeat")){
            text.take()
            val cond = parseExpression()
            val cmds = parseInstructionBlock()
            AST.Repeat(cond, cmds)
        }
        else if (text.isKeyword("while")){
            text.take()
            val cond = parseExpression()
            val cmds = parseInstructionBlock()
            AST.While(cond, cmds)
        }
        else if (text.isKeyword("ask")){
            text.take()
            val cmds = parseInstructionBlock()
            AST.Ask(cmds)
        }
        else if (text.isKeyword("list")){
            text.take()
            AST.ListValue(List(parseExpression(), parseExpression()))
        }
        else if (text.isDelimiter("[")){
            text.take()
            val reporter = parseExpression()
            text.requierToken(DelimiterToken("]"))
            text.requierToken(DelimiterToken("of"))
            val from = text.getIdentifier()
            AST.OfValue(reporter, from)
        }
        else if (text.isDelimiter("(")){
            text.take()
            val buffer = ArrayBuffer[Expression]()
            text.requierToken(KeywordToken("list"))
            while(!text.isDelimiter(")")){
                buffer.addOne(parseExpression())
            }
            text.requierToken(DelimiterToken(")"))
            AST.ListValue(buffer.toList)
        }
        else{
            throw new UnexpectedTokenException(text.take(), EOFToken())
        }
    }

    /**
     * Parse an instruction block delimited by [ ]
     */ 
    def parseInstructionBlock()(implicit text: TokenBufferedIterator, context: Context): AST = {
        text.requierToken(DelimiterToken("["))

        val buffer = ListBuffer[AST]()
        while(!text.isDelimiter("]")){
            buffer.addOne(parseIntruction())
        }

        text.requierToken(DelimiterToken("]"))
        AST.Block(buffer.toList)
    }

    /**
     * Parse function call with or without args
     */
    def parseCall(iden: String)(implicit text: TokenBufferedIterator, context: Context): Expression = {
        val fun = context.getFunction(iden)
        val args = ArrayBuffer[Expression]()
        var i = 0
        for( i <- 0 to fun.argsNames.length - 1){
            args.addOne(parseExpression())
        }
        AST.Call(iden, args.toList)
    }

    /**
     * Parse a function call or a variable 
     */
    def parseSimpleExpression()(implicit text: TokenBufferedIterator, context: Context): Expression = {
        val token = text.take() 
        token match{
            case IdentifierToken(iden) => {
                if (context.hasFunction(iden)){
                    parseCall(iden)
                }
                else {
                    AST.VariableValue(iden)
                }
            }
            case DelimiterToken("(") => {
                val expr = parseExpression()
                text.requierToken(DelimiterToken(")"))
                expr
            }
            case IntLitToken(value)    => AST.IntValue(value)
            case BoolLitToken(value)   => AST.BooleanValue(value)
            case StringLitToken(value) => AST.StringValue(value)
            case FloatLitToken(value)  => AST.FloatValue(value)
        }
    }
    
    /**
     * Parse an expression
     */ 
    def parseExpression(opIndex: Int = 0)(implicit text: TokenBufferedIterator, context: Context): Expression = {
        def rec(): Expression = {
            if (opIndex + 1 == operatorsPriority.size){
                return parseSimpleExpression()
            }
            else{
                return parseExpression(opIndex + 1)
            }
        }

        var left = rec()
        val ops = operatorsPriority(opIndex)
        var op = text.getOperator(ops)
        while(op.nonEmpty){
            left = AST.BinarayExpr(op.get, left, rec())
            op = text.getOperator(ops)
        }
        return left
    }

    /**
     * Return List of Variable
     */
    def getVariablesGroup()(implicit text: TokenBufferedIterator): List[String] = {
        text.requierToken(DelimiterToken("["))
        text.setStart()
        text.takeWhile(x => x match {
                    case DelimiterToken("]") => false
                    case _ => true 
            })
        val tokens = text.cut().map(_ match {
            case IdentifierToken(name) => name
            case found => throw new UnexpectedTokenException(found, IdentifierToken("_"))
        })
        text.requierToken(DelimiterToken("]"))

        tokens
    }

    /**
     * Return List of Arguments of a function
     */ 
    def getArgsName()(implicit text: TokenBufferedIterator, context: Context): List[String] = {
        text.peek() match{
                    case DelimiterToken("[") => getVariablesGroup()
                    case _ => List()
                }
    }

    /**
     * Return unparsed body of a function
     */
    def getFunctionBody()(implicit text: TokenBufferedIterator, context: Context): List[Token] = {
        text.setStart()
        text.takeWhile(x => x match {
            case KeywordToken("end") => false
            case _ => true 
        })

        val body = text.cut()

        text.requierToken(KeywordToken("end"))
        body
    }
}