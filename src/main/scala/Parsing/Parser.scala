package parsing

import ast.Tree
import Tokens._

object Parser{
    def parse(text: TokenBufferedIterator) = {
        val c = text.take()
        c match{
            case KeywordToken("to") => {
                
            }
        }
    }
}