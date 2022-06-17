# How to use
Put source file in `compiler/src/main/resources/demo` with extension .nlogo

In sbt do `project compiler` then `run` and enter file name without extension. Output is exported to `runner/src`

Main function must be called `go`. Setup function must be called `setup`

# Currently Not Supported
- _with_ expressions with variable belonging to other agents
- _list_ of value
- UI related functions
- Some operations on agents

# Code Structure
`scala/parsing/Lexer`: Split code into token

`scala/parsing/Parser`: Parse Token with 2 phases. One for breed, globals and another for functions body.

`scala/analyser/BreedAnalyser`: Assign functions to breeds

`scala/analyser/NameAnalyser`: Map Variable to an object

`scala/analyser/TypeChecker`: Find type of variables

`scala/codegen`: Generate output code


`scala/utils/Context`: Object to store/get objects of the programms like variables, breeds and functions.

`scala/utils/ContextMap`: Object to variable in a scope.
