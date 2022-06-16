# How to use
Put source file in `compiler/src/main/resources/demo` with extension .nlogo

In sbt do `project compiler` then `run` and enter file name without extension. Output is exported to `runner/src`

Main function must be called `go`. Setup function must be called `setup`

# Currently Not Supported
- _with_ expressions with variable belonging to other agents
- _list_ of value
- UI related functions
- Some operations on agents