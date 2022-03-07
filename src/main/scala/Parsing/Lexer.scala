package parsing

import java.io.File
import Tokens._

object Lexer{
    val delimiter = List('[', ']')
    val operator = List('+', '-', '*', '/')
    val keyword = List()
    
    def parse(text: StringBufferedIterator, acc: TokenBuffer):TokenBuffer = {
        text.setStart()
        if (text.hasNext){
            val c: Char = text.take()

            if (c.isDigit){
                text.takeWhile( x => x.isDigit)
                if (text.peek() == '.'){
                    text.takeWhile(x => x.isDigit)
                    parse(text, acc.add(FloatLitToken(text.cut().toFloat), text))
                }
                else{
                    parse(text, acc.add(IntLitToken(text.cut().toInt), text))
                }
            }
            else if (c.isLetter){
                text.takeWhile(x => x.isLetterOrDigit || x == '-')
                parse(text, acc.add(DelimiterToken(text.cut()), text))
            }
            else if (c.isSpaceChar){
                text.takeWhile(x => x.isSpaceChar || x == '-')
                parse(text, acc.add(SpaceToken(), text))

            }
            else if (delimiter.contains(c)){
                parse(text, acc.add(DelimiterToken(text.cut()), text))
            }
            else if (operator.contains(c)){
                text.takeWhile(x => operator.contains(x))
                parse(text, acc.add(OperatorToken(text.cut()), text))
            }
            else if (c == ';'){
                text.takeWhile(x => x != '\n')
                parse(text, acc.add(CommentToken(text.cut()), text))
            }
            else{
                parse(text, acc.add(ErrorToken(text.cut()), text))
            }
        }
        else{
            acc
        }
    }
}