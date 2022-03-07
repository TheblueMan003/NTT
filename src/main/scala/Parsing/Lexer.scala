package parsing

import java.io.File
import Tokens._

object Lexer{
    val delimiter = List('[', ']')
    
    def parse(text: StringBufferedIterator, acc: List[Token]):List[Token] = {
        text.setStart()
        val c: Char = text.take()

        if (c.isDigit()){
            while(text.peek().isDigit){
                text.take()
            }
        }
        else if (c.isLetter()){
            while(text.peek().isWordChar){
                text.take()
            }
            parse(text, acc :: List(DelimiterToken(c.toString).setPosition(text)))
        }
        else if (delimiter.contains(c)){
            parse(text, acc :: List(DelimiterToken(c.toString).setPosition(text)))
        }
    }
}