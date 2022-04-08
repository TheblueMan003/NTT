package parsing

import java.io.File
import Tokens._
import utils.{TokenBufferBuilder,StringBufferedIterator,Reporter}

object Lexer{
    val delimiter = List('[', ']', '(', ')')
    val operator = Set("+", "-", "*", "/", "<", "=", ">", "!", "and", "or", "xor", "mod")
    val identifier = ".?=*!<>:#+/%$_^'&-".toCharArray
    val keyword = Set("breed", "directed-link-breed", "end", "extensions", "globals", 
                        "__includes", "to", "to-report", "of", "list",
                        "undirected-link-breed", "with",
                        "let", "set", "ask", "tick",
                        "if", "ifelse", "ifelse-value")
    
    // TODO add position to token for errors
    def tokenize(text: StringBufferedIterator, acc: TokenBufferBuilder = new TokenBufferBuilder()):TokenBufferBuilder = {
        text.setStart()
        if (text.hasNext){
            val c: Char = text.take().toLower // Net-Logo is case insensitive
            
            // Number
            if (c.isDigit){
                text.takeWhile( x => x.isDigit)
                if (text.peek() == '.'){
                    text.take()
                    text.takeWhile(x => x.isDigit)
                    val cut = text.cut()
                    tokenize(text, acc.add(FloatLitToken(cut._1.toFloat).pos(cut._2), text))
                }
                else{
                    val cut = text.cut()
                    tokenize(text, acc.add(IntLitToken(cut._1.toInt).pos(cut._2), text))
                }
            }
            else if (c == '"'){
                text.takeWhile(x => x != '"')
                text.take()
                val cut = text.cut()
                tokenize(text, acc.add(StringLitToken(cut._1.substring(1, cut._1.length()-1)).pos(cut._2), text))
            }
            // Delimiter
            else if (delimiter.contains(c)){
                val cut = text.cut()
                tokenize(text, acc.add(DelimiterToken(cut._1).pos(cut._2), text))
            }
            // Identifier or Keyword
            else if (c.isLetter || identifier.contains(c)){
                text.takeWhile(x => x.isLetterOrDigit || identifier.contains(x))
                val cut = text.cut()
                if (keyword.contains(cut._1)){
                    tokenize(text, acc.add(KeywordToken(cut._1).pos(cut._2), text))
                }
                else if (operator.contains(cut._1)){
                    tokenize(text, acc.add(OperatorToken(cut._1).pos(cut._2), text))
                }
                else{
                    tokenize(text, acc.add(IdentifierToken(cut._1).pos(cut._2), text))
                }
            }
            // Comment
            else if (c == ';'){
                text.takeWhile(x => x != '\n')
                val cut = text.cut()
                tokenize(text, acc.add(CommentToken(cut._1).pos(cut._2), text))
            }
            // Spaces Chars
            else if (c.isSpaceChar || c == '\n'){
                text.takeWhile(x => x.isSpaceChar || x == '\n')
                val cut = text.cut()
                tokenize(text, acc.add(SpaceToken().pos(cut._2), text))

            }
            else{
                val cut = text.cut()
                Reporter.error(f"Unknown Token ${cut._2.positionString()}")
                tokenize(text, acc.add(ErrorToken(cut._1).pos(cut._2), text))
            }
        }
        else{
            acc
        }
    }
}