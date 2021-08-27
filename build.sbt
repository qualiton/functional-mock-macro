import Dependencies.autoImport._
import sbt.Keys.version
import sbt.addCompilerPlugin

lazy val commonSettings = List(
  version := "0.1",
  scalaVersion := "2.13.4",
  scalafmtOnCompile := true,
//  scalacOptions ++= "-Ymacro-annotations" :: "-language:experimental.macros" :: "-Ymacro-debug-lite" :: Nil,
  scalacOptions ++= "-Ymacro-annotations" :: "-language:experimental.macros" :: Nil,
  Global / onChangedBuildSource := ReloadOnSourceChanges,
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.0" cross CrossVersion.full)
)

lazy val root = (project in file("."))
  .aggregate(`scala-macros`, `scala-macros-usage`)
  .settings(commonSettings)

lazy val `scala-macros` = (project in file("scala-macros"))
  .settings(commonSettings)
  .withDependencies

lazy val `scala-macros-usage` = (project in file("scala-macros-usage"))
  .dependsOn(`scala-macros`)
  .settings(commonSettings)
  .withDependencies
