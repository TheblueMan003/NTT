package codegen

import ast.Variable
import java.io.File
import java.io.PrintWriter
import java.net.URL

trait Generator{
    def generate(indentation: Int): String
}
case class ClassFile(val name: String, val fields: List[InstructionGen], val functions: List[FunctionGen], val parent: List[String], val imports: List[String]) extends Generator{
    def generate(indentation: Int): String = {
        val content = InstructionBlock(fields ::: functions)
        InstructionCompose(f"@lift\nclass $name", content).generate(indentation)
    }
    def writeToFile(path: String) = {
        val file = new File(path)
        val pw = new PrintWriter(file)
        pw.write(generate(0))
        pw.close
    }
}
case class FunctionGen(val name: String, val returnType: String, val content: Instruction) extends Generator{
    def generate(indentation: Int): String = {
        InstructionCompose(f"def ${name}():$returnType = ", content).generate(indentation)
    }
}
trait Instruction extends Generator
case class InstructionGen(val value: String) extends Instruction{
    def generate(indentation: Int): String = {
        "\t"*indentation + value
    }
}
case class InstructionCompose(val prefix: String, val postfix: Generator) extends Instruction{
    def generate(indentation: Int): String = {
        val sCont = postfix.generate(indentation)
        "\t" * indentation + prefix + sCont
    }
}
case class InstructionBlock(val content: List[Generator]) extends Instruction{
    def generate(indentation: Int): String = {
        val sCont = content.map(_.generate(indentation + 1)).foldLeft("")(_ + "\n" + _)
        "{" + sCont + "\n" + "\t" * indentation + "}"
    }
}
case class InstructionList(val content: List[Generator]) extends Instruction{
    def generate(indentation: Int): String = {
        val sCont = content.map(_.generate(indentation)).foldLeft("")(_ + "\n" + _)
        sCont
    }
}

