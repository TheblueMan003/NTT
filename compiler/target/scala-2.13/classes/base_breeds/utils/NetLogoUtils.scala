package example
package netlogo
import scala.collection.Iterable

object NetLogoUtils{
    def toList[T](value: Any): List[T] ={
        value match {
            case l: Iterable[T] => l.toList
            case _ => List[T](value.asInstanceOf[T])
        }
    }
    def filterPosition[T](value: Iterable[(T,Int,Int)], x: Double, y: Double): List[T] ={
        value.filter(e => e._2 == x.asInstanceOf[Int] && e._3 == y.asInstanceOf[Int]).map(x => x._1).toList
    }
    def filterPositions[T](value: Iterable[(T,Int,Int)], pos: List[(Int, Int)]): List[T] ={
        value.filter(e => pos.contains((e._2, e._3))).map(x => x._1).toList
    }
}