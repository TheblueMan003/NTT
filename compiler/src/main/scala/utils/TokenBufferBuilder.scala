package utils

import scala.collection.mutable

import parsing.Token
import parsing.Tokens

class TokenBufferBuilder(val list: mutable.ArrayBuffer[Token] = new mutable.ArrayBuffer[Token]()){
    /**
      * Add a token to the buffer.
      *
      * @param token: Token to add to the buffer.
      * @return this
      */
    def add(token: Token): TokenBufferBuilder = {
        list.addOne(token)
        this
    }

    /**
      * Transforms to a TokenBufferedIterator
      *
      * @return TokenBufferedIterator
      */
    def toIterator(): TokenBufferedIterator = {
        new TokenBufferedIterator(list.filter(
            _ match {
                case Tokens.CommentToken(_) => false
                case Tokens.SpaceToken() => false
                case _ => true  
            }
        ).toList)
    }

    /**
      * Append the given tokens to the buffer.
      *
      * @param other: The tokens to append.
      * @return The new buffer.
      */
    def union(other: TokenBufferBuilder):TokenBufferBuilder = {
        new TokenBufferBuilder(list ++ other.list)
    } 
}