package parsing

import ast.AST
import ast.AST.Expression
import Tokens._
import utils.Context
import netlogo.UnlinkedFunction
import utils.VariableOwner
import utils.TokenBufferedIterator
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer
import netlogo.BreedClass._
import utils.Reporter
import java.awt.RenderingHints.Key

object Parser{
    
    private var operatorsPriority = List(List("with"), List("^"), List("*", "/", "mod"), List("+", "-"), List("<", ">", "<=", ">="), List("=", "!="), List("and","or", "xor")).reverse

    def parse(text: TokenBufferedIterator): Context = {
        val con = new Context()

        Reporter.debug("Phase 1 - Function Discovery")

        // Phase 1 - Function & Breed Discovery
        functionDiscovery()(text, con)

        Reporter.debug("Phase 2 - Variable to Breed")

        // Phase 1.5 - Add All owned variable to their breed
        con.breedVariableOwnSetup()

        Reporter.debug("Phase 2.5 - Function Parsing")

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
            
                context.addFunction(name, argName, body, false)
            }

            case KeywordToken("to-report") => {
                val name = text.getIdentifier()
                val argName = getArgsName()
                val body = getFunctionBody()
            
                context.addFunction(name, argName, body, true)
            }

            case KeywordToken("breed") => {
                val names = getVariablesGroup()
                if (names.size != 2) throw new Exception("Wrong Number of names for breed")
                context.addBreed(names(1), names(0), TurtleBreed())
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
                    context.addOwned("observer", _)
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
                case cf: UnlinkedFunction => {
                    Reporter.debug("Parsing function " + cf.name)
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
        val token = text.peek()
        val tree = if (text.isKeyword("let")){ //let <var> <expr>
            text.take()
            val iden = text.getIdentifier()
            val value = parseExpression()
            AST.Declaration(AST.VariableValue(iden), value)
        }
        else if (text.isKeyword("set")){ //set <var> <expr>
            text.take()
            val iden = text.getIdentifier()
            val value = parseExpression()
            AST.Assignment(AST.VariableValue(iden), value)
        }
        else if (text.isIdentifier()){
            val token = text.peek()
            val iden = text.getIdentifier()
            if (iden.startsWith("create-") && context.hasBreedPlural(iden.drop("create-".length()))){ //create-<breed> <expr>
                val breed = context.getBreedPlural(iden.drop("create-".length()))
                val nb = parseExpression()
                val block = parseInstructionBlock()
                AST.CreateBreed(AST.BreedValue(breed), nb, block)
            }
            else if (iden.startsWith("hatch-") && context.hasBreedPlural(iden.drop("hatch-".length()))){ // hatch-<breed> <nb> <block>
                val breed = context.getBreedPlural(iden.drop("hatch-".length()))
                val nb = parseExpression()
                val block = parseInstructionBlock()
                AST.Ask(AST.BreedValue(context.getObserverBreed()), AST.CreateBreed(AST.BreedValue(breed), nb, block))
            }
            else if (iden.startsWith("hatch")){ //hatch <nb> <block>
                val breed = context.getTurtleBreed()
                val nb = parseExpression()
                val block = parseInstructionBlock()
                AST.HatchBreed(AST.BreedValue(breed), nb, block)
            }
            else if (iden.startsWith("sprout-") && context.hasBreedPlural(iden.drop("sprout-".length()))){ // sprout-<breed> <nb> <block>
                val breed = context.getBreedPlural(iden.drop("sprout-".length()))
                val nb = parseExpression()
                val block = parseInstructionBlock()
                AST.Ask(AST.BreedValue(context.getObserverBreed()), AST.CreateBreed(AST.BreedValue(breed), nb, block))
            }
            else if (iden.startsWith("sprout")){ //sprout <nb> <block>
                val breed = context.getTurtleBreed()
                val nb = parseExpression()
                val block = parseInstructionBlock()
                AST.Ask(AST.BreedValue(context.getObserverBreed()), AST.CreateBreed(AST.BreedValue(breed), nb, block))
            }
            else if (context.hasFunction(iden)){ //function call
                parseCall(iden, false)
            }
            else{
                throw new Exception(f"Unknown function: ${iden} at ${token.positionString()}")
            }
        }
        else if (text.isKeyword("report")){ //if <expr> [block]
            text.take()
            val expr = parseExpression()

            AST.Report(expr)
        }
        else if (text.isKeyword("if")){ //if <expr> [block]
            text.take()
            val cond = parseExpression()
            val cmds = parseInstructionBlock()

            AST.IfBlock(cond, cmds)
        }
        else if (text.isKeyword("ifelse")){ //ifelse (<expr> [block])* <block>
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
        else if (text.isKeyword("loop")){ //loop [block]
            text.take()
            val cmds = parseInstructionBlock()
            AST.Loop(cmds)
        }
        else if (text.isKeyword("repeat")){ //repeat <expr> [block]
            text.take()
            val cond = parseExpression()
            val cmds = parseInstructionBlock()
            AST.Repeat(cond, cmds)
        }
        else if (text.isKeyword("while")){ //while <expr> [block]
            text.take()
            val cond = parseExpression()
            val cmds = parseInstructionBlock()
            AST.While(cond, cmds)
        }
        else if (text.isKeyword("ask")){ //while <expr> [block]
            text.take()
            val agent = parseExpression()
            val cmds = parseInstructionBlock()
            AST.Ask(agent, cmds)
        }
        else if (text.isKeyword("list")){ //list <expr> <expr>
            text.take()
            AST.ListValue(List(parseExpression(), parseExpression()))
        }
        else if (text.isDelimiter("(")){ //(list expr*)
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
        tree.setPosition(token)
        tree
    }

    /**
     * Parse an instruction block delimited by [ ]
     */ 
    def parseInstructionBlock()(implicit text: TokenBufferedIterator, context: Context): AST.Block = {
        val token = text.peek()
        text.requierToken(DelimiterToken("["))

        val buffer = ListBuffer[AST]()
        while(!text.isDelimiter("]")){
            buffer.addOne(parseIntruction())
        }

        text.requierToken(DelimiterToken("]"))
        val tree = AST.Block(buffer.toList)
        tree.setPosition(token)
        tree
    }

    /**
     * Parse function call with or without args
     */
    def parseCall(iden: String, isExpr: Boolean)(implicit text: TokenBufferedIterator, context: Context): Expression = {
        val fun = context.getFunction(iden)
        val args = ArrayBuffer[Expression]()
        var i = 0
        for( i <- 0 to fun.argsNames.length - 1){
            args.addOne(parseExpression(1))
        }
        AST.Call(iden, args.toList)
    }

    /**
     * Parse a reporter call
     */
    def parseReporter()(implicit text: TokenBufferedIterator, context: Context): Expression = {
        text.requierToken(DelimiterToken("["))
        val reporter = parseExpression()
        text.requierToken(DelimiterToken("]"))
        reporter
    }

    /**
     * Parse a function call or a variable 
     */
    def parseSimpleExpression()(implicit text: TokenBufferedIterator, context: Context): Expression = {
        val token = text.take()
        val tree = token match{
            case(IdentifierToken("min-one-of")) => {
                AST.MinOneAgent(AST.SortBy(parseSimpleExpression(), parseReporter()))
            }
            case(IdentifierToken("max-one-of")) => {
                AST.MaxOneAgent(AST.SortBy(parseSimpleExpression(), parseReporter()))
            }
            case(IdentifierToken("min-n-of")) => {
                val turtles = parseSimpleExpression()
                val nb = parseSimpleExpression()
                val reporter = parseReporter()
                AST.MinNAgent(AST.SortBy(turtles, reporter), nb)
            }
            case(IdentifierToken("max-n-of")) => {
                val turtles = parseSimpleExpression()
                val nb = parseSimpleExpression()
                val reporter = parseReporter()
                AST.MaxNAgent(AST.SortBy(turtles, reporter), nb)
            }
            case(IdentifierToken("neighbors")) => { 
                AST.Neighbors
            }
            case(IdentifierToken("nobody")) => { 
                AST.Nobody
            }
            case(IdentifierToken("all?")) => { 
                val turtles = parseSimpleExpression()
                val reporter = parseReporter()
                AST.All(turtles, reporter)
            }
            case(IdentifierToken("any?")) => { 
                AST.Any(parseSimpleExpression())
            }
            case(IdentifierToken("count")) => { 
                AST.Count(parseSimpleExpression())
            }
            case(IdentifierToken("other")) => { 
                val turtles = parseSimpleExpression()
                AST.Other(turtles)
            }
            case(IdentifierToken("patch")) => { 
                AST.BreedAtSingle(AST.BreedValue(context.getBreedSingular("patch")), parseSimpleExpression(), parseSimpleExpression())
            }
            case(IdentifierToken("one-of")) => { 
                if (text.hasNext() && text.peek() == DelimiterToken("[")){
                    text.requierToken(DelimiterToken("["))
                    var content = List[Expression]()
                    while (text.hasNext() && text.peek() != DelimiterToken("]")){
                        content = parseSimpleExpression() :: content
                    }
                    text.requierToken(DelimiterToken("]"))
                    AST.OneOfValue(content)
                }
                else{
                    val turtles = parseSimpleExpression()
                    AST.OneOf(turtles)
                }
            }
            case(IdentifierToken("patch-ahead")) => {
                val angle = parseSimpleExpression()
                val x = AST.BinarayExpr("+", AST.Call("cos", List(angle)), AST.VariableValue("pycor"))
                val y = AST.BinarayExpr("+", AST.Call("sin", List(angle)), AST.VariableValue("pycor"))
                AST.BreedAt(AST.BreedValue(context.getPatchBreed()), x, y)
            }
            case(IdentifierToken("patch-at-heading-and-distance")) => {
                val angle = parseSimpleExpression()
                val distance = parseSimpleExpression()
                val x = AST.BinarayExpr("+", AST.BinarayExpr("*", distance, AST.Call("cos", List(angle))), AST.VariableValue("pycor"))
                val y = AST.BinarayExpr("+", AST.BinarayExpr("*", distance, AST.Call("sin", List(angle))), AST.VariableValue("pycor"))
                AST.BreedAt(AST.BreedValue(context.getPatchBreed()), x, y)
            }
            case(IdentifierToken("patch-left-and-ahead")) => {
                val angle = parseSimpleExpression()
                val distance = parseSimpleExpression()
                val x = AST.BinarayExpr("+", AST.BinarayExpr("*", distance, AST.Call("cos", List(AST.BinarayExpr("+", AST.VariableValue("heading"), angle)))), AST.VariableValue("pycor"))
                val y = AST.BinarayExpr("+", AST.BinarayExpr("*", distance, AST.Call("sin", List(AST.BinarayExpr("+", AST.VariableValue("heading"), angle)))), AST.VariableValue("pycor"))
                AST.BreedAt(AST.BreedValue(context.getPatchBreed()), x, y)
            }
            case(IdentifierToken("patch-right-and-ahead")) => {
                val angle = parseSimpleExpression()
                val distance = parseSimpleExpression()
                val x = AST.BinarayExpr("+", AST.BinarayExpr("*", distance, AST.Call("cos", List(AST.BinarayExpr("-", AST.VariableValue("heading"), angle)))), AST.VariableValue("pycor"))
                val y = AST.BinarayExpr("+", AST.BinarayExpr("*", distance, AST.Call("sin", List(AST.BinarayExpr("-", AST.VariableValue("heading"), angle)))), AST.VariableValue("pycor"))
                AST.BreedAt(AST.BreedValue(context.getPatchBreed()), x, y)
            }
            case IdentifierToken(iden) => {
                // Breed-at
                if (iden.endsWith("-at")){
                    val name = iden.dropRight("-at".length())
                    if (context.hasBreedPlural(name)){
                        AST.BreedAt(AST.BreedValue(context.getBreedPlural(iden.dropRight("-at".length()))), parseSimpleExpression(), parseSimpleExpression())
                    }
                    else{
                        AST.BreedAtSingle(AST.BreedValue(context.getBreedSingular(iden.dropRight("-at".length()))), parseSimpleExpression(), parseSimpleExpression())
                    }
                }// Breed-here
                else if (iden.endsWith("-here")){
                    val name = iden.dropRight("-here".length())
                    if (context.hasBreedPlural(name)){
                        AST.BreedAt(AST.BreedValue(context.getBreedPlural(name)), AST.VariableValue("pxcor"), AST.VariableValue("pycor"))
                    }
                    else{
                        AST.BreedAtSingle(AST.BreedValue(context.getBreedSingular(name)), AST.VariableValue("pxcor"), AST.VariableValue("pycor"))
                    }
                }// Breed-on
                else if (iden.endsWith("-on")){
                    AST.BreedOn(AST.BreedValue(context.getBreedPlural(iden.dropRight("-on".length()))), parseSimpleExpression())
                }
                else if (context.hasBreedPlural(iden)){
                    AST.BreedValue(context.getBreedPlural(iden))
                }
                else if (context.hasFunction(iden)){
                    parseCall(iden, true)
                }
                else{
                    AST.VariableValue(iden)
                }
            }

            
            case DelimiterToken("(") => {
                val expr = parseExpression()
                text.requierToken(DelimiterToken(")"))
                expr
            }

            // Literals
            case IntLitToken(value)    => AST.IntValue(value)
            case BoolLitToken(value)   => AST.BooleanValue(value)
            case StringLitToken(value) => AST.StringValue(value)
            case FloatLitToken(value)  => AST.FloatValue(value)

            //[expr] of <identifier>
            case DelimiterToken("[") => { //[expr] of <identifier>
                val reporter = parseExpression()
                text.requierToken(DelimiterToken("]"))
                text.requierToken(KeywordToken("of"))
                val from = parseSimpleExpression()
                AST.OfValue(reporter, from)
            }

            // If statement
            case KeywordToken("ifelse-value") => { //ifelse (<expr> [expr])* <expr>
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
            
            // Unary operators
            case OperatorToken("-") => {
                val expr = parseExpression()
                AST.BinarayExpr("-", AST.IntValue(0), expr)
            }
            // Unary operators
            case KeywordToken("not") => {
                AST.Not(parseExpression())
            }

            case other => throw new UnexpectedTokenException(other, "Any Expression Token")
        }
        tree.setPosition(token)
        tree
    }
    
    /**
     * Parse an expression
     */ 
    def parseExpression(opIndex: Int = 0)(implicit text: TokenBufferedIterator, context: Context): Expression = {
        def rec(): Expression = {
            if (opIndex + 1 == operatorsPriority.size){
                parseSimpleExpression()
            }
            else{
                parseExpression(opIndex + 1)
            }
        }

        // Get Left Part of potential binary expression
        val token = text.peek()
        var left = rec()
        left.setPosition(token)

        // Get Operator according to priority
        val ops = operatorsPriority(opIndex)
        if (ops == List("with")){
            if (text.hasNext && text.peek() == OperatorToken("with")){
                text.take()
                text.requierToken(DelimiterToken("["))
                val predicate = parseExpression(0)
                text.requierToken(DelimiterToken("]"))
                left = AST.WithValue(left, predicate)
                left.setPosition(token)
                left
            }
            else{
                left
            }
        }
        else{
            var op = text.getOperator(ops)
            while(op.nonEmpty){
                left = AST.BinarayExpr(op.get, left, rec())
                left.setPosition(token)
                op = text.getOperator(ops)
            }
            left
        }
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