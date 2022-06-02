ThisBuild / organization := "ch.epfl.data"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "1.3-SNAPSHOT"

val project_name = "NetLogo Compiler"
name := project_name

val paradiseVersion = "2.1.0"
val breezeVersion = "0.13.2"
val scalaTestVersion = "3.0.0"
val squidVersion = "0.4.1-SNAPSHOT"
val sparkVersion = "3.0.1"
val graphVizVersion = "0.10.0"
val akkaVersion = "2.6.14"
val scalapbVersion = "1.0.6"

run / fork := true

lazy val commonSettings = Seq(
  libraryDependencies += "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  libraryDependencies += "org.scalanlp" %% "breeze" % breezeVersion,
  libraryDependencies += "org.scalanlp" %% "breeze-natives" % breezeVersion,
  libraryDependencies += "org.scalanlp" %% "breeze-viz" % breezeVersion,
)

lazy val squidSettings = Seq(
  autoCompilerPlugins := true,
  addCompilerPlugin(
    "org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full
  ),
  unmanagedBase := (unmanagedBase in LocalRootProject).value
)

lazy val akkaSettings = Seq(
  libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3",
  libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  libraryDependencies += "com.typesafe.akka" %% "akka-cluster-typed"         % akkaVersion,
  libraryDependencies += "com.typesafe.akka" %% "akka-serialization-jackson" % akkaVersion,
)

lazy val sparkSettings = Seq(
  libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion,
  libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion,
)

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
    commonSettings, squidSettings,
    Test / parallelExecution := false
  )

lazy val genExample = (project in file("generated"))
  .settings(
    name := f"${project_name}-genExample",
    Test / parallelExecution := false,
    commonSettings, akkaSettings, sparkSettings,
  )
  .dependsOn(runner)