package codegen

import analyser.SymTree._
import analyser.SymTree
import utils.Context
import ast.Breed
import ast.{LinkedFunction, Function, BaseFunction}
import ast.Variable
import analyser.Types._
import analyser.Type
import utils.Reporter

object CodeGen{
    var varCounter = 0

    def generate(context: Context): List[ClassFile] = {
        varCounter = 0
        generateMainClass(context) ::
            List(generateMainInit(context)):::
            context.getBreeds().map(generate(_, context)).toList
    }
    private def generate(breed: Breed, context: Context): ClassFile = {
        if (breed == context.getObserverBreed()){
            ClassFile(
                List(),
                "@lift",
                "class",
                breed.className,
                List("Actor"),
                generateObserverSets(context):::
                    generateVariables(breed.getAllVariables()), 
                generateFunctions(breed.getAllFunctions(), breed, context)
            )
        }
        else{
            ClassFile(
                List(),
                "@lift",
                "class",
                breed.className,
                List("Actor"),
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
        val observer = context.getObserverBreed().className
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
        val lst = if (breeds.size > 0) {breeds.map(b => b.singularName).reduce(_ + ", "+_)} else {""}
        InstructionGen(f"compileSims(List($lst), Some(mainClass))")
    }
    private def generateClassWithObject(breed: Breed):Instruction = {
        InstructionGen(f"val ${breed.singularName} : ClassWithObject[${breed.className}] = ${breed.className}.reflect(IR)")
    }
    private def generateObserverSets(context: Context): List[Instruction] = {
        context.getBreeds().map(b =>
            InstructionGen(f"val ${Renamer.toValidName(b.pluralName)} = mutable.Set[${b.className}]()")
        ).toList
    }


    private def generateFunctions(function: Iterable[Function], breed: Breed, context: Context): List[FunctionGen] = {
        generateMainFunction(breed, context)::
        function.filter(_.name != "go").map{
            _ match {
                case lc: LinkedFunction => generate(lc, breed)
                case _ => null
            }
        }.filter(_ != null).toList
    }
    private def generate(function: LinkedFunction, breed: Breed): FunctionGen = {
        FunctionGen(Renamer.toValidName(function.name), function._args, Type.toString(function.getType()), generate(function.symTree)(function, breed))
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
        InstructionCompose(f"def set_${name}(__value : ${typ}): Unit = ", 
            InstructionBlock(List(
                InstructionGen(f"${name} = __value"))
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
                fct match{
                    case fct: LinkedFunction => {
                        val argsInstr = args.map(generateExpr(_)).reduce(_ + ", "+ _)
                        InstructionGen(f"${fct.name}($argsInstr)")
                    }
                    case fct: BaseFunction =>{
                        InstructionGen(fct.call(args.map(generateExpr(_))))
                    }
                }
            }
            case CreateBreed(b, nb, fct) => {
                InstructionList(List(
                    InstructionGen(f"${Renamer.toValidName(b.name.pluralName)}.add(new ${b.name.className}())")
                ))
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

            case Report(expr) => InstructionGen(f"return ${generateExpr(expr)}")

            case Tick => {
                if (function.name != "go"){
                 Reporter.warning("Tick command not supported outside of go method")
                }
                InstructionGen("waitLabel(Turn, 1)")
            }
            case Ask(upperCaller, turtles, block) => {
                val set = generateExpr(turtles)
                val fct = block.name
                varCounter += 1
                InstructionList(List(
                    InstructionGen(f"val tmp$varCounter = $set.toList.map(s => asyncMessage(() => s.$fct(this)))"),
                    InstructionCompose(f"while(!tmp$varCounter.forall(_.isCompleted))", InstructionBlock(List(
                        InstructionGen("waitAndReply(1)"))
                    ))
                ))
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
            case BreedValue(breed) => Renamer.toValidName(breed.pluralName)
            case ListValue(lst) => ???

            case IfElseBlockExpression(conds, block) => {
                (conds.map(c => f"if (${generateExpr(c._1)})${generateExpr(c._2)}")
                     ::: List(generateExpr(block)))
                     .foldLeft("")((x,y) => f"${x} else ${y}")
            }

            case BinarayExpr(op, lf, rt) => f"(${generateExpr(lf)} ${getOperator(op)} ${generateExpr(rt)})"

            case Call(fct, args) => {
                fct match{
                    case fct: LinkedFunction => {
                        val argsInstr = args.map(generateExpr(_)).reduce(_ + ", "+ _)
                        InstructionGen(f"${fct.name}($argsInstr)").generate(0)
                    }
                    case fct: BaseFunction =>{
                        InstructionGen(fct.call(args.map(generateExpr(_)))).generate(0)
                    }
                }
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
        val go = if (breed == context.getObserverBreed()){
            val fct = breed.getFunction("go").asInstanceOf[LinkedFunction]
            InstructionList(generate(fct.symTree)(fct, breed).asInstanceOf[InstructionBlock].content)
        }
        else{
            InstructionGen("")
        }

        FunctionGen("main", List(), "Unit", 
            InstructionBlock(List(
                init,
                InstructionCompose(f"while(true)", 
                InstructionBlock(List(
                    go,
                    InstructionGen("handleMessages()"),
                    InstructionGen("waitLabel(Turn, 1)")
                ))
                )  
            ))
        )
    }
}