package  utils

import scala.collection.mutable.Map
import scala.collection.mutable.Stack

case class ContextMap[T](){
    private val stack = new Stack[Map[String, T]]()

    push()

    def add(key: String, value: T) = {
        stack.top.addOne((key, value))
    }
    def contains(key: String):Boolean = {
        stack.exists(_.contains(key))
    }
    def get(key: String): T = {
        stack.map( m => {
            if (m.contains(key)){
                return m.get(key).get
            }
        }
        )
        throw new Exception(f"Element with key ${key} not found!")
    }
    def push() = {
        stack.push(Map())
    }
    def pop() = {
        stack.pop()
    }
}