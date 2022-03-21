package parsing

import ast.Tree
import Tokens._
import utils.Context
import utils.CompiledFunction
import utils.VariableOwner
import utils.TokenBufferedIterator
import scala.collection.mutable.ArrayBuffer
import ast.Expression
import scala.collection.mutable.ListBuffer
import ast.BreedType._

object Parser{
    
    private var operatorsPriority = List(List("<", ">", "<=", ">=" ,"=","!="), List("^"), List("*", "/", "and"), List("+", "-", "or", "xor")).reverse

    def parse(text: TokenBufferedIterator): Context = {
        val con = new Context()

        // Phase 1 - Function & Breed Discovery
        functionDiscovery()(text, con)

        // Phase 1.5 - Add All owned variable to their breed
        con.breadVariableOwnSetup()

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
    def parseFunctionsBody()(implicit text: TokenBufferedIterator, context: Context):Tree = {
        val body = ListBuffer[Tree]()
        while(text.hasNext()){
            body.addOne(parseIntruction())
        }
        return Tree.Block(body.toList)
    }


    /**
     * Parse an instruction
     */ 
    def parseIntruction()(implicit text: TokenBufferedIterator, context: Context): Tree = {
        if (text.isKeyword("let")){
            text.take()
            val iden = text.getIdentifier()
            val value = parseExpression()
            ???
        }
        else if (text.isKeyword("set")){
            text.take()
            val iden = text.getIdentifier()
            val vari = context.getVariable(iden)
            val value = parseExpression()
            Tree.Assignment(vari, value)
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

            Tree.IfBlock(cond, cmds)
        }
        else if (text.isKeyword("ifelse")){
            text.take()
            val buffer = ListBuffer[(Expression, Tree)]()

            while(!text.isDelimiter("[")){
                val cond = parseExpression()
                val cmds = parseInstructionBlock()
                buffer.addOne((cond, cmds))
            }

            val cmds = parseInstructionBlock()

            Tree.IfElseBlock(buffer.toList, cmds)
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

            Tree.IfElseBlockExpression(buffer.toList, value)
        }
        else if (text.isKeyword("loop")){
            text.take()
            val cmds = parseInstructionBlock()
            Tree.Loop(cmds)
        }
        else if (text.isKeyword("repeat")){
            text.take()
            val cond = parseExpression()
            val cmds = parseInstructionBlock()
            Tree.Repeat(cond, cmds)
        }
        else if (text.isKeyword("while")){
            text.take()
            val cond = parseExpression()
            val cmds = parseInstructionBlock()
            Tree.While(cond, cmds)
        }
        else if (text.isKeyword("ask")){
            text.take()
            val cmds = parseInstructionBlock()
            Tree.Ask(cmds)
        }
        else{
            throw new UnexpectedTokenException(text.take(), EOFToken())
        }
    }

    /**
     * Parse an instruction block delimited by [ ]
     */ 
    def parseInstructionBlock()(implicit text: TokenBufferedIterator, context: Context): Tree = {
        text.requierToken(DelimiterToken("["))

        val buffer = ListBuffer[Tree]()
        while(!text.isDelimiter("]")){
            buffer.addOne(parseIntruction())
        }

        text.requierToken(DelimiterToken("]"))
        Tree.Block(buffer.toList)
    }

    /**
     * Parse function call with or without args
     */
    def parseCall(iden: String)(implicit text: TokenBufferedIterator, context: Context): Expression = {
        val fun = context.getFunction(iden)
        val args = ArrayBuffer[Tree]()
        var i = 0
        for( i <- 0 to fun.argsNames.length - 1){
            args.addOne(parseExpression())
        }
        Tree.Call(iden, args.toList)
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
                    Tree.UnlinkedVariable(iden)
                }
            }
            case DelimiterToken("(") => {
                val expr = parseExpression()
                text.requierToken(DelimiterToken(")"))
                expr
            }
            case IntLitToken(value)    => Tree.IntValue(value)
            case BoolLitToken(value)   => Tree.BooleanValue(value)
            case StringLitToken(value) => Tree.StringValue(value)
            case FloatLitToken(value)  => Tree.FloatValue(value)
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
            left = Tree.BinarayExpr(op.get, left, rec())
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