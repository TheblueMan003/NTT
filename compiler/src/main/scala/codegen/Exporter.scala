package codegen

import scala.io.Source
import java.io.PrintWriter

object Exporter{
    val extraFiles = List(
        "NetLogoUtils.scala"
    )
    def export(code: List[ClassFile], path: String)={
        code.map(c => c.writeToFile(f"$path/${c.name}.scala"))
        extraFiles.map(f => {
            Source.fromResource(f"base_breeds/utils/$f")
            val pw = new PrintWriter(f"$path/$f")
            pw.write(Source.fromResource(f"base_breeds/utils/$f").getLines().reduce(_ + "\n" +_))
            pw.close
        })
    }
}