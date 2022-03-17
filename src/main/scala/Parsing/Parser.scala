package parsing

import ast.Tree
import Tokens._
import utils.Context
import utils.VariableOwner
import scala.collection.mutable.ArrayBuffer
import java.lang.module.ModuleDescriptor.Exports
import ast.Expression
import scala.collection.mutable.ListBuffer

object Parser{

    private var operatorsPriority = List(List("<", ">", "<=", ">=" ,"=","!="), List("^"), List("*", "/"), List("+", "-")).reverse

    def parse(text: TokenBufferedIterator): Context = {
        val con = new Context()
        functionDiscovery()(text, con)
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
                val body = getBody()
            
                context.addFunction(name, List(), body)
            }
            case KeywordToken("to-report") => {
                val name = text.getIdentifier()
                val argName = getArgsName()
                val body = getBody()
            
                context.addFunction(name, List(), body)
            }
            case KeywordToken("globals") => {
                getVariablesGroup().map(
                    context.addVariable(_, VariableOwner.Global())
                )
            }
            case KeywordToken("turtles-own") => {
                getVariablesGroup().map(
                    context.addVariable(_, VariableOwner.TurtlesOwned())
                )
            }
            case KeywordToken("patches-own") => {
                getVariablesGroup().map(
                    context.addVariable(_, VariableOwner.PatchesOwned())
                )
            }
        }
        if (text.hasNext()){
            functionDiscovery()
        }
    }
    
    def parseAllFuncitonsBody()(context: Context) = {
        context.functions.values.map( f =>
            f.body = parseFunctionsBody()(new TokenBufferedIterator(f.tokenBody), context)
        )
    }

    /**
     * Parse inside of Function Body
     */
    def parseFunctionsBody()(implicit text: TokenBufferedIterator, context: Context):Tree = {
        val body = ListBuffer[Tree]()
        while(text.hasNext()){
            body.addOne(parseCallOrVariable())
        }
        return Tree.Block(body.toList)
    }

    /**
     * Parse a function call or a variable 
     */
    def parseCallOrVariable()(implicit text: TokenBufferedIterator, context: Context): Expression = {
        val iden = text.getIdentifier()
        if (context.hasFunction(iden)){
            val fun = context.getFunction(iden)
            val args = ArrayBuffer[Tree]()
            var i = 0
            for( i <- 0 to fun.argsNames.length){
                args.addOne(parseExpression())
            }

            Tree.Call(iden, args.toList)
        }
        else if (context.hasVariable(iden)){
            val var_ = context.getVariable(iden)
            Tree.Variable(iden, var_.getOwner())
        }
        else{
            ???
        }
    }
    
    /**
     * Parse an expression
     */ 
    def parseExpression(opIndex: Int = 0)(implicit text: TokenBufferedIterator, context: Context): Expression = {
        def rec(): Expression = {
            if (opIndex + 1 == operatorsPriority.size){
                return parseCallOrVariable()
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
    def getBody()(implicit text: TokenBufferedIterator, context: Context): List[Token] = {
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