package parsing

import java.io.File
import Tokens._

object Lexer{
    val delimiter = List('[', ']')
    val operator = List('+', '-', '*', '/', '<', '=', '>')
    val keyword = List("breed", "directed-link-breed", "end", "extensions", "globals", 
                        "__includes", "links-own", "patches-own", "to", "to-report", 
                        "turtles-own", "undirected-link-breed")
    
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
                val value = text.cut()
                if (keyword.contains(value)){
                    parse(text, acc.add(KeywordToken(value), text))
                }
                else{
                    parse(text, acc.add(IdentifierToken(value), text))
                }
            }
            else if (c.isSpaceChar || c == '\n'){
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