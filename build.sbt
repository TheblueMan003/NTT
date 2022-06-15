ThisBuild / organization := "ch.epfl.data"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "1.3-SNAPSHOT"

val project_name = "NetLogo Compiler"
name := project_name

val sparkVersion = "3.0.1"


lazy val compiler = (project in file("compiler")).settings(
     inThisBuild(List(
       organization := "ch.epfl.scala",
       scalaVersion := "2.13.3"
     )),
     name := "compiler"
   )
  .settings(
      libraryDependencies ++= Seq(
        "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
        "org.scalatest" %% "scalatest" % "3.2.11" % Test,
        "org.scalameta" %% "scalafmt-dynamic" % "3.5.4"
        )
  )

lazy val runner = (project in file("runner"))
  .settings(
    name := "runner",
    Test / parallelExecution := false
  )