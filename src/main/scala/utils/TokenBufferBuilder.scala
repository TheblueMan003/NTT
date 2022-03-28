package utils

import parsing.Token
import parsing.Tokens

import java.util.ArrayList
import scala.collection.JavaConverters._

class TokenBufferBuilder{
    val list = new ArrayList[Token]()
    
    def add(token: Token, buffer: StringBufferedIterator): TokenBufferBuilder = {
        list.add(token)
        this
    }

    def toIterator(): TokenBufferedIterator = {
        new TokenBufferedIterator(list.asScala.filter(
            _ match {
                case Tokens.CommentToken(_) => false
                case Tokens.SpaceToken() => false
                case _ => true  
            }
        ).toList)
    }
}