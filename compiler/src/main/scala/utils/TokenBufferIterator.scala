package utils
import parsing.Token
import parsing.Tokens._
import parsing.{UnexpectedTokenException, UnexpectedEOFException}

class TokenBufferedIterator(string: List[Token]) extends Positionable{
    var itIndex = 0
    var start = -1

    /**
      * Returns the next token in the buffer and consumes it.
      *
      * @return Token
      */
    def take():Token = {
        val c = string(itIndex)
        itIndex += 1
        c
    }
    /**
      * Consumes the next tokens in the buffer while the predicate is true.
      *
      * @param predicate: (Token) => Boolean The predicate to test the tokens against.
      */
    def takeWhile(predicate: Token=>Boolean) = {
        while (hasNext() && predicate(peek())){
            take()
        }
    }

    /**
      * @return the next token in the buffer without consuming it.
      */
    def peek():Token ={
        string(itIndex)
    }

    /**
      * Set the start of the cut
      */
    def setStart() = {
        start = itIndex
    }

    /**
      * @return list of tokens from the start of the cut to the current position
      */
    def cut(): List[Token] = {
        string.slice(start, itIndex)
    }

    /**
     * @return true if there are more tokens in the buffer
     *        false otherwise
     */
    def hasNext(): Boolean = {
        itIndex < string.length
    }

    /**
     * force the next token to be equal to token
     */
    def requierToken(token: Token) = {
        if (!hasNext()){
            throw new UnexpectedEOFException()
        }
        else if (peek() != token){
            throw new UnexpectedTokenException(peek(), token)
        }
        else{
            take()
        }
    }

    /**
     * @return true is next is a IdentifierToken
     */
    def isIdentifier(): Boolean = {
        if (!hasNext()){
            false
        }
        else{
            peek() match{
                case IdentifierToken(name) => true
                case token => false
            }
        }
    }

    /**
     * @return return the name of the identifier in next
     */
    def getIdentifier(): String = {
        if (!hasNext()){
            throw new UnexpectedEOFException()
        }
        else{
            take() match{
                case IdentifierToken(name) => name
                case token => throw new UnexpectedTokenException(token, IdentifierToken("_"))
            }
        }
    }

    /**
     * @return true is next is a KeywordToken(value)
     */
    def isKeyword(value: String): Boolean = {
        if (!hasNext()){
            false
        }
        else{
            peek() match{
                case KeywordToken(name) => name == value
                case token => false
            }
        }
    }

    /**
     * @return true is next is a KeywordToken(value)
     */
    def isDelimiter(value: String): Boolean = {
        if (!hasNext()){
            false
        }
        else{
            peek() match{
                case DelimiterToken(name) => name == value
                case token => false
            }
        }
    }

    /**
     * @return true is next is a KeywordToken(value)
     */
    def isLiteralValue(value: String): Boolean = {
        if (!hasNext()){
            false
        }
        else{
            peek() match{
                case DelimiterToken(name) => name == value
                case token => false
            }
        }
    }

    /**
     * @return Some(operator) if next is an operator token and None otherwise
     */
    def getOperator(possible: List[String]): Option[String] = {
        if (!hasNext()){
            None
        }
        else{
            peek() match{
                case OperatorToken(name) => { 
                    if (possible.contains(name)){
                        take()
                        Some(name)
                    }
                    else{
                        None
                    }
                }
                case token => None
            }
        }
    }
}