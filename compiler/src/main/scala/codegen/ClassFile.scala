package codegen

import netlogo.Variable
import java.io.File
import java.io.PrintWriter
import java.net.URL
import org.scalafmt.interfaces.Scalafmt
import java.nio.file._

trait Generator{
    def generate(indentation: Int): String
}

object Tool{
    val scalafmt = Scalafmt.create(this.getClass.getClassLoader)
}

case class ClassFile(val imports: List[String], val anotation: String, val prefix: String, val name: String, val parent: List[String], val fields: List[Generator], val functions: List[FunctionGen]) extends Generator{
    def generate(indentation: Int): String = {
        val content = InstructionBlock(fields ::: functions)
        val importsString = imports.foldLeft("")(Renamer.concatLine(_, _))
        var classLine = f"$prefix $name"
        if (parent.nonEmpty){
            classLine = classLine + " extends "+parent.reduce(_ + " " + _)
        }
        InstructionCompose(f"$importsString\n$anotation\n"+classLine, content).generate(indentation)
    }
    def writeToFile(path: String) = {
        val file = new File(path)
        val pw = new PrintWriter(file)
    
        val config = Paths.get(".scalafmt.conf")
        val file2 = Paths.get(path)
            
        pw.write(Tool.scalafmt.format(config, file2, generate(0)))
        pw.close
    }
}
case class FunctionGen(val name: String, val args: List[Variable], val returnType: String, val content: Instruction, val isTickTalkOverride: Int = 0, val isScalaOverride: Boolean = false) extends Generator{
    def generate(indentation: Int): String = {
        val argsStr = if (args.size > 0){args.map(v => Renamer.toValidName(v.name)+f" : ${v.getType()}").reduce(_ + ", " + _)}else {""}
        val overrideStr = if (isScalaOverride || isTickTalkOverride > 0) "override" else ""
        InstructionCompose(f"$overrideStr def $name($argsStr):$returnType = ", content).generate(indentation)
    }
}
trait Instruction extends Generator
case class InstructionGen(val value: String) extends Instruction{
    def generate(indentation: Int): String = {
        "\t"*indentation + value
    }
}
object EmptyInstruction extends Instruction{
    def generate(indentation: Int): String = {
        ""
    }
}
case class InstructionCompose(val prefix: String, val middle: Generator, val postfix: String = "") extends Instruction{
    def generate(indentation: Int): String = {
        val sCont = middle.generate(indentation)
        "\t" * indentation + prefix + sCont + postfix
    }
}


case class InstructionBlock(val content: List[Generator]) extends Instruction{
    def generate(indentation: Int): String = {
        val sCont = content.filter(_ != EmptyInstruction).map(_.generate(indentation + 1)).foldLeft("")(Renamer.concatLine(_, _))
        "{\n" + sCont + "\n" + "\t" * indentation + "}"
    }
}
object InstructionBlock {
    def apply(content: Generator*): InstructionBlock = {
        InstructionBlock(content.toList)
    }
}
case class InstructionList(val content: List[Generator]) extends Instruction{
    def generate(indentation: Int): String = {
        val sCont = content.filter(_ != EmptyInstruction).map(_.generate(indentation)).foldLeft("")(Renamer.concatLine(_, _))
        sCont
    }
}
object InstructionList {
    def apply(content: Generator*): InstructionList = {
        InstructionList(content.toList)
    }
}