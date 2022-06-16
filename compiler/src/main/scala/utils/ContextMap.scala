package  utils

import netlogo.{Variable}

import scala.collection.mutable.Map
import scala.collection.mutable.Stack

class ContextMap[T](){
    private val stack = new Stack[Map[String, (Int, T)]]()
    private val ownerStack = new Stack[Variable]()

    push()

    /**
      * Add a variable to the context.
      *
      * @param key
      * @param value
      * @return
      */
    def add(key: String, value: T) = {
        stack.top.addOne((key, (getAskIndex(), value)))
    }
    
    /**
      * Return true if the key is in the current context
      *
      * @param key
      * @return
      */
    def contains(key: String):Boolean = {
        stack.exists(_.contains(key))
    }

    /**
      * Returns the value in the current context with the given key.
      *
      * @param key
      * @return
      */
    def get(key: String): T = {
        stack.map(m => {
            if (m.contains(key)){
                return m.get(key).get._2
            }
        })
        throw new Exception(f"Element with key ${key} not found!")
    }

    /**
      * Returns the value of the variable with the given key and the owner of it
      *
      * @param key
      * @return (owner, value)
      */
    def getWithOwner(key: String): (Variable, T) = {
        stack.map(m => {
            if (m.contains(key)){
                val value = m.get(key).get
                if (getAskIndex() > 0){
                    val owner = ownerStack(value._1 - 1)
                    return (owner, value._2)
                }
                else{
                    return (null, value._2)
                }
            }
        })
        throw new Exception(f"Element with key ${key} not found!")
    }

    /**
      * Returns the value of the variable with the given key and the index in the call of ask stack of its owner
      *
      * @param key
      * @return (index, value)
      */
    def getWithOwnerIndex(key: String): (Int, T) = {
        stack.map(m => {
            if (m.contains(key)){
                val value = m.get(key).get
                if (value._1 == getAskIndex()){
                    return (-1, value._2)
                }
                else{
                    return (getAskIndex() - value._1 - 1, value._2)
                }
            }
        })
        throw new Exception(f"Element with key ${key} not found!")
    }

    /**
      * Pushes a new block to the stack
      */
    def push() = {
        stack.push(Map())
    }

    /**
      * Pops the top block from the stack
      */
    def pop() = {
        stack.pop()
    }

    /**
      * Push an ask block with the given owner to the stack
      *
      * @param owner: myself
      */
    def pushAsk(owner: Variable) = {
        ownerStack.push(owner)
    }

    /**
      * Pop the top ask block from the stack
      */
    def popAsk(): Variable = {
        ownerStack.pop()
    }

    /**
      * Returns the index of the top ask block in the call stack
      *
      * @return index
      */
    def getAskIndex() = {
        ownerStack.size
    }

    /**
     * Returns the variables on the stack
     *
     * @return variables
     */
    def getAskVariables():List[Variable] = {
        ownerStack.toList
    }
}