package codegen

import scala.io.Source
import java.io.PrintWriter
import java.io.File

object Exporter{
    val extraFiles = List(
        "NetLogoUtils.scala"
    )

    /**
      * Exports the given codes to the given directory.
      *
      * @param code: The code to export.
      * @param path: The path of the directory.
      * @return
      */
    def export(code: List[ClassFile], path: String)={
        val directory = new File(path)
        if (! directory.exists()){
            directory.mkdir()
        }
        // export the code
        code.map(c => c.writeToFile(f"$path/${c.name}.scala"))

        // add the extra files
        extraFiles.map(f => {
            Source.fromResource(f"base_breeds/utils/$f")
            val pw = new PrintWriter(f"$path/$f")
            pw.write(Source.fromResource(f"base_breeds/utils/$f").getLines().reduce(_ + "\n" +_))
            pw.close
        })
    }
}