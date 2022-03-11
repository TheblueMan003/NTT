package parsing

import utils._
import java.util.ArrayList
import scala.collection.JavaConverters._

class TokenBuffer{
    val list = new ArrayList[Token]()
    
    def add(token: Token, buffer: StringBufferedIterator): TokenBuffer = {
        token.setPosition(buffer)
        list.add(token)
        this
    }

    def toIterator(): TokenBufferedIterator = {
        new TokenBufferedIterator(list.asScala.filter(_ != Tokens.SpaceToken()).toList)
    }
}