package codegen

import ast.Variable

trait Generator{
    def generate(indentation: Int): String
}
case class ClassFile(val fields: List[Variable], val functions: List[FunctionGen], val parent: List[String], val imports: List[String]) extends Generator{
    def generate(indentation: Int): String = {
        ""
    }
}
case class FunctionGen(val name: String, val content: Intruction) extends Generator{
    def generate(indentation: Int): String = {
        InstructionGen(f"def ${name}():Unit = ").generate(indentation)
    }
}
trait Intruction extends Generator
case class InstructionGen(val value: String) extends Intruction{
    def generate(indentation: Int): String = {
        "\t"*indentation + value
    }
}
case class IntructionBlock(val prefix: String, val content: List[Intruction]){
    def generate(indentation: Int): String = {
        val sCont = content.map(_.generate(indentation + 1)).foldLeft("")(_ + "\n" + _)
        "\t" * indentation + prefix + "{\n" + sCont + "\n" + "\t" * indentation + "}"
    }
}
