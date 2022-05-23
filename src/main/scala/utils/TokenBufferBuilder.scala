package utils

import scala.collection.mutable

import parsing.Token
import parsing.Tokens

class TokenBufferBuilder(val list: mutable.ArrayBuffer[Token] = new mutable.ArrayBuffer[Token]()){
    
    def add(token: Token, buffer: StringBufferedIterator): TokenBufferBuilder = {
        list.addOne(token)
        this
    }

    def toIterator(): TokenBufferedIterator = {
        new TokenBufferedIterator(list.filter(
            _ match {
                case Tokens.CommentToken(_) => false
                case Tokens.SpaceToken() => false
                case _ => true  
            }
        ).toList)
    }

    def union(other: TokenBufferBuilder):TokenBufferBuilder = {
        new TokenBufferBuilder(list ++ other.list)
    } 
}