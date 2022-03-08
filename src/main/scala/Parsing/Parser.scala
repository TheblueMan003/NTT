package parsing

import ast.Tree
import Tokens._
import utils.Context
import utils.VariableOwner
import scala.collection.mutable.ArrayBuffer

object Parser{
    def functionDiscovery()(implicit text: TokenBufferedIterator, context: Context): Unit = {
        val c = text.take()
        c match{
            case KeywordToken("to") => {
                val name = text.getIdentifier()
                text.setStart()
                text.takeWhile(x => x match {
                    case KeywordToken("end") => false
                    case _ => true 
                })

                val body = text.cut()

                text.requierToken(KeywordToken("end"))

                context.addFunction(name, List(), body)
            }
            case KeywordToken("globals") => {
                parseVariablesGroup().map(
                    context.addVariable(_, VariableOwner.Global())
                )
            }
            case KeywordToken("turtles-own") => {
                parseVariablesGroup().map(
                    context.addVariable(_, VariableOwner.TurtlesOwned())
                )
            }
            case KeywordToken("patches-own") => {
                parseVariablesGroup().map(
                    context.addVariable(_, VariableOwner.PatchesOwned())
                )
            }
        }
        if (text.hasNext()){
            functionDiscovery()
        }
    }

    def parseFunctionBody()(implicit text: TokenBufferedIterator) = {
        val body = ArrayBuffer[Tree]()
        while(text.hasNext()){
            body.addOne(parseCallOrVariable())
        }
    }

    def parseCallOrVariable()(implicit text: TokenBufferedIterator): Tree = ???
    def parseExpression()(implicit text: TokenBufferedIterator): Tree = ???

    def parseVariablesGroup()(implicit text: TokenBufferedIterator): List[String] = {
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
}