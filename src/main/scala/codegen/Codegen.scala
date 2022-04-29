package codegen

import analyser.SymTree._
import analyser.SymTree
import utils.Context
import ast.Breed
import ast.{LinkedFunction, Function}
import ast.Variable
import analyser.Types._
import analyser.Type
import utils.Reporter

object CodeGen{
    def generate(context: Context): List[ClassFile] = {
        generateMainClass(context) ::
            List(generateMainInit(context)):::
            context.getBreeds().map(generate(_, context)).toList
    }
    private def generate(breed: Breed, context: Context): ClassFile = {
        if (breed == context.getObserverBreed()){
            ClassFile(
                List(),
                "@Lift",
                "class",
                breed.singularName,
                List(),
                generateObserverSets(context):::
                    generateVariables(breed.getAllVariables()), 
                generateFunctions(breed.getAllFunctions(), breed, context)
            )
        }
        else{
            ClassFile(
                List(),
                "@Lift",
                "class",
                Renamer.toClassName(breed.singularName),
                List(),
                generateVariables(breed.getAllVariables()), 
                generateFunctions(breed.getAllFunctions(), breed, context)
            )
        }
    }
    private def generateMainInit(context: Context): ClassFile = {
        ClassFile(
            List(),
            "",
            "object",
            "MainInit",
            List(""),
            generateMainInitContent(context), 
            List(),
        )
    }
    private def generateMainInitContent(context: Context):List[Instruction] = {
        val observer = context.getObserverBreed().singularName
        List(
        InstructionCompose("val liftedMain = meta.classLifting.liteLift",
        InstructionBlock(List(
            InstructionCompose("def apply(): List[Actor] = ", InstructionBlock(List(
                InstructionGen(f"List(new ${observer}())")
            )))
        ))))
    }

    private def generateMainClass(context: Context): ClassFile = {
        ClassFile(
            List(),
            "",
            "object",
            "Simulation",
            List("App"),
            generateMainClassContent(context: Context), 
            List(),
        )
    }
    private def generateMainClassContent(context: Context):List[Instruction] = {
        InstructionGen("val mainClass = MainInit.liftedMain")::
            context.getBreeds().map(generateClassWithObject(_)).toList :::
            List(generateMainClassStart(context))
    }
    private def generateMainClassStart(context: Context):Instruction = {
        val breeds = context.getBreeds()
        val lst = if (breeds.size > 0) {breeds.map(b => Renamer.toClassName(b.singularName)).reduce(_ + ", "+_)} else {""}
        InstructionGen(f"compileSims(List($lst), Some(mainClass))")
    }
    private def generateClassWithObject(breed: Breed):Instruction = {
        InstructionGen(f"val ${breed.singularName} : ClassWithObject[${Renamer.toClassName(breed.singularName)}] = Reader.reflect(IR)")
    }
    private def generateObserverSets(context: Context): List[Instruction] = {
        context.getBreeds().map(b =>
            InstructionGen(f"val ${b.pluralName} = Mutable.Set[${Renamer.toClassName(b.singularName)}]()")
        ).toList
    }


    private def generateFunctions(function: Iterable[Function], breed: Breed, context: Context): List[FunctionGen] = {
        generateMainFunction(breed, context)::
        function.map{
            _ match {
                case lc: LinkedFunction => generate(lc, breed)
                case _ => null
            }
        }.filter(_ != null).toList
    }
    private def generate(function: LinkedFunction, breed: Breed): FunctionGen = {
        FunctionGen(function.name, Type.toString(function.getType()), generate(function.symTree)(function, breed))
    }
    private def generateVariables(variables: Iterable[Variable]): List[Instruction] = {
        val exported = variables.filter(_.exported)
        exported.map(generate(_)).toList ::: exported.map(generateGetterSetter(_)).toList.flatten
    }
    private def generate(variable: Variable): Instruction = {
        val typ = variable.getType()
        InstructionGen(f"var ${Renamer.toValidName(variable.name)} : ${Type.toString(typ)} = ${Type.defaultValue(typ)}")
    }
    private def generateGetterSetter(variable: Variable): List[Instruction] = {
        val typ = Type.toString(variable.getType())
        val name = Renamer.toValidName(variable.name)
        List(
        InstructionGen(f"def get_${name}(): ${typ} = ${name}"),
        InstructionCompose(f"def set_${name}(__value__ : ${typ}): Unit = ", 
            InstructionBlock(List(
                InstructionGen(f"${name} = __value__"))
            ))
        )
    }
    
    private def generate(instr: SymTree)(implicit function: Function, breed: Breed): Instruction = {
        instr match{
            case Block(body) => {
                val content = body.map(generate(_))
                InstructionBlock(content)
            }
            case Declaration(VariableValue(v), value) => InstructionGen(f"${v.name} = ${generateExpr(value)}")
            case Assignment(VariableValue(v), value) => InstructionGen(f"${v.name} = ${generateExpr(value)}")
            case Call(fct, args) => {
                val argsInstr = args.map(generateExpr(_)).reduce(_ + ", "+ _)
                InstructionGen(f"${fct.name}($argsInstr)")
            }
            case CreateBreed(b, nb, fct) => {
                InstructionGen(f"val __tmp__ = new ${b.name.singularName}()")
            }

            case IfBlock(cond, block) => InstructionCompose(f"if(${generateExpr(cond)})", generate(block))
            case IfElseBlock(conds, block) => {
                InstructionList(
                    InstructionCompose(f"if(${generateExpr(conds.head._1)})", generate(conds.head._2)) ::
                    conds.drop(1).map(c => 
                        InstructionCompose(f"else if(${generateExpr(c._1)})", generate(c._2))
                    ) ::: List(
                        InstructionCompose("else", generate(block))
                    )
                )
            }

            case Loop(block) => InstructionCompose("while(true)", generate(block))
            case Repeat(number, block) => ???
            case While(cond, block) => InstructionCompose(f"while(${generateExpr(cond)})", generate(block))

            case Tick => {
                if (function.name != "go"){
                 Reporter.warning("Tick command not supported outside of go method")
                }
                InstructionGen("waitLabel(Turn, 1)")
            }
        }
    }
    private def generateExpr(expr: Expression): String = {
        expr match{
            case VariableValue(v) => Renamer.toValidName(v.name)
            case BooleanValue(v) => v.toString()
            case IntValue(v) => v.toString()
            case FloatValue(v) => v.toString()
            case StringValue(v) => "\"" + v.toString() + "\""
            case BreedValue(breed) => ???
            case ListValue(lst) => ???

            case IfElseBlockExpression(conds, block) => {
                (conds.map(c => f"if (${generateExpr(c._1)})${generateExpr(c._2)}")
                     ::: List(generateExpr(block)))
                     .foldLeft("")((x,y) => f"${x} else ${y}")
            }

            case BinarayExpr(op, lf, rt) => f"(${generateExpr(lf)} ${getOperator(op)} ${generateExpr(rt)})"

            case Call(fct, args) => {
                val argsInstr = args.map(generateExpr(_)).reduce(_ + ", "+ _)
                InstructionGen(f"${fct.name}($argsInstr)").generate(0)
            }
        }
    }

    private def getOperator(op: String): String = {
        op match {
            case "=" => "=="
            case _ => op
        }
    }

    private def generateMainFunction(breed: Breed, context: Context): FunctionGen = {
        val init = if (breed == context.getObserverBreed()){
            InstructionGen("setup()")
        }
        else{
            InstructionGen("")
        }
        FunctionGen("main", "Unit", 
            InstructionBlock(List(
                init,
                InstructionCompose(f"while(true)", 
                InstructionBlock(List(
                    InstructionGen("handleMessages()"),
                    InstructionGen("waitLabel(Turn, 1)")
                ))
                )  
            ))
        )
    }
}