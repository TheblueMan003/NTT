package parsing

import java.util.ArrayList

class TokenBuffer{
    val list = new ArrayList[Token]()
    
    def add(token: Token, buffer: StringBufferedIterator): TokenBuffer = {
        token.setPosition(buffer)
        list.add(token)
        this
    }
}