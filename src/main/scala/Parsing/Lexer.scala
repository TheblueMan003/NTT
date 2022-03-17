package parsing

import java.io.File
import Tokens._
import utils.{TokenBufferBuilder,StringBufferedIterator}

object Lexer{
    val delimiter = List('[', ']')
    val operator = List('+', '-', '*', '/', '<', '=', '>', '!')
    val identifier = List('-', '?')
    val keyword = List("breed", "directed-link-breed", "end", "extensions", "globals", 
                        "__includes", "to", "to-report", 
                        "undirected-link-breed")
    
    def parse(text: StringBufferedIterator, acc: TokenBufferBuilder):TokenBufferBuilder = {
        text.setStart()
        if (text.hasNext){
            val c: Char = text.take()

            // Number
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
            // Identifier or Keyword
            else if (c.isLetter){
                text.takeWhile(x => x.isLetterOrDigit || identifier.contains(x))
                val value = text.cut()
                if (keyword.contains(value)){
                    parse(text, acc.add(KeywordToken(value), text))
                }
                else{
                    parse(text, acc.add(IdentifierToken(value), text))
                }
            }
            // Spacec Chars
            else if (c.isSpaceChar || c == '\n'){
                text.takeWhile(x => x.isSpaceChar || x == '-')
                parse(text, acc.add(SpaceToken(), text))

            }
            // Delimiter
            else if (delimiter.contains(c)){
                parse(text, acc.add(DelimiterToken(text.cut()), text))
            }
            // Operator
            else if (operator.contains(c)){
                text.takeWhile(x => operator.contains(x))
                parse(text, acc.add(OperatorToken(text.cut()), text))
            }
            // Comment
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