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
        generateAllClass()(context)
    }
    def generateAllClass()(implicit context: Context) = {
        generateMainClass() ::
            List(generateMainInit()):::
            context.getBreeds().map(generateBreed(_)).toList
    }

    /**
     * Generate Breed Class.
     *
     * @param	breed	
     * @return	ClassFile
     */
    private def generateBreed(breed: Breed)(implicit context: Context): ClassFile = {
        ClassFile(
            List(),
            "@lift",
            "class",
            breed.className,
            getParents
            (breed),
            generateBreedFields(breed), 
            generateBreedFunctions(breed)
        )
    }

    /**
     * Generate Field for breed Class.
     *
     * @param	breed	
     * @return	List[Instruction] 
     */
    private def generateBreedFields(breed: Breed)(implicit context: Context):List[Instruction] = {
        if (breed == context.getObserverBreed()){
            generateObserverSets():::generateVariables(breed.getAllVariables())
        } else {
            generateVariables(breed.getAllVariables())
        }
    }

    /**
     * Generate MainInit Class.
     *
     * @return	ClassFile
     */
    private def generateMainInit()(implicit context: Context): ClassFile = {
        ClassFile(
            List(),
            "",
            "object",
            "MainInit",
            List(""),
            generateMainInitContent(), 
            List(),
        )
    }

    /**
     * Generate MainInit Class Content.
     *
     * @return	Instruction
     */
    private def generateMainInitContent()(implicit context: Context):List[Instruction] = {
        val observer = context.getObserverBreed().className
        List(
        InstructionCompose("val liftedMain = meta.classLifting.liteLift",
        InstructionBlock(List(
            InstructionCompose("def apply(): List[Actor] = ", InstructionBlock(List(
                InstructionGen(f"List(new ${observer}())")
            )))
        ))))
    }

    /**
     * Generate Main Class.
     *
     * @return	ClassFile
     */
    private def generateMainClass()(implicit context: Context): ClassFile = {
        ClassFile(
            List(),
            "",
            "object",
            "Simulation",
            List("App"),
            generateMainClassContent(), 
            List(),
        )
    }

    /**
     * Generate Main Class Content.
     *
     * @return	List[Instruction]
     */
    private def generateMainClassContent()(implicit context: Context):List[Instruction] = {
        InstructionGen("val mainClass = MainInit.liftedMain")::
            context.getBreeds().map(generateClassWithObject(_)).toList :::
            List(generateMainClassStart())
    }

    /**
     * Generate MainInit Class Content.
     *
     * @return	Instruction
     */
    private def generateMainClassStart()(implicit context: Context):Instruction = {
        val breeds = context.getBreeds()
        val lst = if (breeds.size > 0) {breeds.map(b => b.singularName).reduce(_ + ", "+_)} else {""}
        InstructionGen(f"compileSims(List($lst), Some(mainClass))")
    }

    /**
     * Generate Reflect for a breed.
     *
     * @param	breed	
     * @return	Instruction
     */
    private def generateClassWithObject(breed: Breed):Instruction = {
        InstructionGen(f"val ${breed.singularName} : ClassWithObject[${breed.className}] = ${breed.className}.reflect(IR)")
    }

    /**
     * Generate Observer sets.
     *
     * @return	List[Instruction]
     */
    private def generateObserverSets()(implicit context: Context): List[Instruction] = {
        context.getBreeds().map(b =>
            InstructionGen(f"val ${Renamer.toValidName(b.pluralName)} = mutable.Set[${b.className}]()")
        ).toList
    }


    /**
     * Generate Functions for a Breed.
     *
     * @param	breed	
     * @return	List[FunctionGen]
     */
    private def generateBreedFunctions(breed: Breed)(implicit context: Context): List[FunctionGen] = {
        generateMainFunction(breed)::
        breed.getAllFunctions().filter(_.name != "go").map{
            _ match {
                case lc: LinkedFunction => generate(lc, breed)
                case _ => null
            }
        }.filter(_ != null).toList
    }

    /**
     * Generate Function Content.
     *
     * @param	function
     * @param   breed
     * @return	FunctionGen
     */
    private def generate(function: LinkedFunction, breed: Breed)(implicit context: Context): FunctionGen = {
        FunctionGen(Renamer.toValidName(function.name), function._args, Type.toString(function.getType()), generate(function.symTree)(function, breed))
    }

    /**
     * Generate Variables.
     *
     * @param	variables	
     * @return	List[Instruction]
     */
    private def generateVariables(variables: Iterable[Variable])(implicit context: Context): List[Instruction] = {
        val exported = variables.filter(_.exported)
        exported.map(generate(_)).toList ::: exported.map(generateGetterSetter(_)).toList.flatten
    }

    /**
     * Generate Variable.
     *
     * @param	variable
     * @return	Instruction
     */
    private def generate(variable: Variable)(implicit context: Context): Instruction = {
        val typ = variable.getType()
        InstructionGen(f"var ${Renamer.toValidName(variable.name)} : ${Type.toString(typ)} = ${Type.defaultValue(typ)}")
    }

    /**
     * Generate Getter and Setter for variable.
     *
     * @param	variable: Variable for which to generate the setter
     * @return	List[Instruction] containing setter and getter
     */
    private def generateGetterSetter(variable: Variable)(implicit context: Context): List[Instruction] = {
        val typ = Type.toString(variable.getType())
        List(
        InstructionGen(f"def ${variable.getGetterName()}(): ${typ} = ${variable.getName()}"),
        InstructionCompose(f"def ${variable.getSetterName()}(__value : ${typ}): Unit = ", 
            InstructionBlock(List(
                InstructionGen(f"${variable.getName()} = __value"))
            ))
        )
    }
    
    /**
     * Convert SymTree Instruction to Scala Instruction
     *
     * @param	mixed	inst: SymTree	
     * @return	Instruction
     */
    private def generate(instr: SymTree)(implicit function: Function, breed: Breed): Instruction = {
        instr match{
            case Block(body) => {
                val content = body.map(generate(_))
                InstructionBlock(content)
            }
            case Declaration(VariableValue(v), value) => {
                val expr = generateExpr(value)
                InstructionList(List(expr._1, InstructionGen(f"${v.name} = ${expr._2}")))
            }
            case Assignment(VariableValue(v), value) => {
                val expr = generateExpr(value)
                InstructionList(List(expr._1, InstructionGen(f"${v.name} = ${expr._2}")))
            }
            case Call(fct, args) => {
                val (prefix, content) = generateExpr(instr.asInstanceOf[SymTree.Expression])
                InstructionList(List(prefix, InstructionGen(content)))
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
            case While(cond, block) => {
                val expr = generateExpr(cond)
                InstructionList(List(
                    expr._1,
                    InstructionCompose(f"while(${expr._2})", generate(block))
                ))
            }

            case Report(expr) => {
                val expr2 = generateExpr(expr)
                InstructionList(List(
                    expr2._1,
                    InstructionGen(f"${expr2._2}")
                ))
            }

            case Tick => {
                if (function.name != "go"){
                    Reporter.warning("Tick command not supported outside of go method")
                }
                InstructionGen("waitLabel(Turn, 1)")
            }
            case Ask(upperCaller, turtles, block) => {
                val set = generateExpr(turtles)
                val fct = block.name
                val vari = getUniqueVariableName()
                InstructionList(List(
                    set._1,
                    InstructionGen(f"val $vari = ${set._2}.toList.map(s => asyncMessage(() => s.$fct(this)))"),
                    InstructionCompose(f"while(!$vari.forall(_.isCompleted))", InstructionBlock(List(
                        InstructionGen("waitAndReply(1)"))
                    ))
                ))
            }
        }
    }

    /**
     * Convert Sym.Tree Expression to Scala String Expression
     *
     * @param	exp: Expression
     * @return	Scala expression as a string
     */
    private def generateExpr(expr: Expression): (Instruction, String) = {
        expr match{
            case VariableValue(v) => (EmptyInstruction, Renamer.toValidName(v.name))
            case BooleanValue(v) => (EmptyInstruction, v.toString())
            case IntValue(v) => (EmptyInstruction, v.toString())
            case FloatValue(v) => (EmptyInstruction, v.toString())
            case StringValue(v) => (EmptyInstruction, "\"" + v.toString() + "\"")
            case BreedValue(breed) => (EmptyInstruction, Renamer.toValidName(breed.pluralName))
            case ListValue(lst) => ???
            case OfValue(expr, from) => {
                val from2 = generateExpr(from)
                val ret = generateOfValue(from2._2, expr.vari)
                (
                    InstructionList(List(from2._1, ret._1)),
                    ret._2
                )
            }

            case IfElseBlockExpression(conds, block) => {
                (
                    EmptyInstruction,
                    (conds.map(c => f"if (${generateExpr(c._1)})${generateExpr(c._2)}")
                        ::: List(generateExpr(block)))
                        .foldLeft("")((x,y) => f"${x} else ${y}")
                )
            }

            case BinarayExpr(op, lf, rt) => {
                val left = generateExpr(lf)
                val right = generateExpr(rt)
                (
                    InstructionList(List(left._1, right._1)),
                    f"(${left._2} ${getOperator(op)} ${right._2})"
                )
            }

            case Call(fct, args) => {
                fct match{
                    case fct: LinkedFunction => {
                        val argsPro = args.map(generateExpr(_))
                        val argsCall = argsPro.map(_._2).reduce(_ + ", "+ _)
                        val argsInstr = argsPro.map(_._1)
                        (
                            InstructionList(argsInstr),
                            InstructionGen(f"${fct.name}($argsCall)").generate(0)
                        )
                    }
                    case fct: BaseFunction =>{
                        val argsPro = args.map(generateExpr(_))
                        val argsCall = argsPro.map(_._2)
                        val argsInstr = argsPro.map(_._1)
                        (
                            InstructionList(argsInstr),
                            InstructionGen(fct.call(argsCall)).generate(0)
                        )
                    }
                }
            }
        }
    }

    private def generateOfValue(set: String, variable: Variable): (Instruction, String) = {
        val vari = getUniqueVariableName()
        val vari2 = getUniqueVariableName()
        val typ = Type.toString(variable.getType())

        (
            InstructionList(List(
                InstructionGen(f"val $vari = $set.toList.map(a => asyncMessage(() => a.${variable.getGetterName()}))"),
                InstructionCompose(f"while (!($vari.nonEmpty && $vari.forall(x => x.isCompleted)))",
                    InstructionBlock(List(
                        InstructionGen("waitAndReply(1)"))
                    )
                ),
                InstructionGen(f"val $vari2: List[$typ] = $vari.map(o => o.popValue.get).asInstanceOf[List[$typ]]")
            )),
            vari2
        )
    }

    /**
     * Convert NetLogo Operator to Scala Operator
     *
     * @param	op: String	
     * @return	Scala operator
     */
    private def getOperator(op: String): String = {
        op match {
            case "=" => "=="
            case _ => op
        }
    }

    /**
     * @param	breed	
     * @return	FunctionGenerator for the main function of the breed
     */
    private def generateMainFunction(breed: Breed)(implicit context: Context): FunctionGen = {
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

        FunctionGen(getMainFunctionName(breed), List(), "Unit", 
            InstructionBlock(List(
                init,
                InstructionCompose(f"while(true)", 
                InstructionBlock(List(
                    go,
                    InstructionGen("handleMessages()"),
                    generateMainFunctionSwitch(breed),
                    InstructionGen("waitLabel(Turn, 1)")
                ))
                )  
            ))
        )
    }

    private def generateMainFunctionSwitch(breed: Breed)(implicit context: Context): Instruction = {
        InstructionCompose(f"DEFAULT_ASK match", InstructionBlock(
            breed.getAllFunctions()
                .filter(_.isAsked)
                .map(_.asInstanceOf[LinkedFunction])
                .map(f => generate(f.symTree)(f, breed))
                .zipWithIndex
                .map(f => InstructionCompose(f"case ${f._2} => ", f._1))
                .toList
        ))
    }

    /**
     * @param	breed
     * @return	List of parents for the breed class
     */
    private def getParents(breed: Breed)(implicit context: Context): List[String] = {
        if (breed.parent == null){
            List()
        }
        else if (breed.parent == context.getBreedSingular("agent")){
            List("Actor")
        }
        else{
            List(breed.parent.className)
        }
    }

    /**
     * @param	breed
     * @return	Name of the main function for the breed class
     */
    private def getMainFunctionName(breed: Breed)(implicit context: Context): String = {
        if (breed.parent == null){
            "main"
        }
        else if (breed.parent == context.getBreedSingular("agent")){
            "main"
        }
        else{
            "override_main"
        }
    }

    private def getUniqueVariableName():String={
        val id = varCounter
        varCounter += 1
        f"tmp_$id"
    }
}