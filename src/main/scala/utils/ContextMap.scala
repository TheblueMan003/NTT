package  utils

import ast.{Variable}

import scala.collection.mutable.Map
import scala.collection.mutable.Stack

case class ContextMap[T](){
    private val stack = new Stack[Map[String, (Int, T)]]()
    private val ownerStack = new Stack[Variable]()

    push()

    def add(key: String, value: T) = {
        stack.top.addOne((key, (getAskIndex(), value)))
    }
    def contains(key: String):Boolean = {
        stack.exists(_.contains(key))
    }
    def get(key: String): T = {
        stack.map(m => {
            if (m.contains(key)){
                return m.get(key).get._2
            }
        })
        throw new Exception(f"Element with key ${key} not found!")
    }
    def getWithOwner(key: String): (Variable, T) = {
        stack.map(m => {
            if (m.contains(key)){
                val value = m.get(key).get
                if (getAskIndex() > 0){
                    val owner = ownerStack(value._1)
                    return (owner, value._2)
                }
                else{
                    return (null, value._2)
                }
            }
        })
        throw new Exception(f"Element with key ${key} not found!")
    }

    def push() = {
        stack.push(Map())
    }
    def pop() = {
        stack.pop()
    }

    def pushAsk(owner: Variable) = {
        ownerStack.push(owner)
    }
    def popAsk(): Variable = {
        ownerStack.pop()
    }
    def getAskIndex() = {
        ownerStack.size
    }
    def getAskVariables():List[Variable] = {
        ownerStack.toList
    }
}