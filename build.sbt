import Dependencies.autoImport._
import org.jetbrains.sbtidea.Keys._
import sbt.Keys.version
import sbt.addCompilerPlugin

lazy val commonSettings = List(
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.13.4",
  name := "functional-mock",
  organization := "org.qualiton",
  organizationName := "Qualiton Ltd",
  scalafmtOnCompile := true,
//  scalacOptions ++= "-Ymacro-annotations" :: "-language:experimental.macros" :: "-Ymacro-debug-lite" :: Nil,
  scalacOptions ++= "-Ymacro-annotations" :: "-language:experimental.macros" :: Nil,
  Global / onChangedBuildSource := ReloadOnSourceChanges,
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.0" cross CrossVersion.full)
)

lazy val root = (project in file("."))
  .aggregate(`ijext`, `macro`, `usage`)
  .settings(commonSettings)
  .settings(
    moduleName := "root",
    publish / skip := true
  )

lazy val `ijext` = (project in file("ijext"))
  .aggregate(`macro`)
  .settings(commonSettings)
  .enablePlugins(SbtIdeaPlugin)
  .settings(
//    crossPaths := false,
    moduleName := "functional-mock-macro-ijext",
    ThisBuild / intellijPluginName := "Functional Mock Injector",
    ThisBuild / intellijBuild := "212.4746.92",
    ThisBuild / intellijPlatform := IntelliJPlatform.IdeaCommunity,
    intellijPlugins += "com.intellij.properties".toPlugin,
    intellijPlugins += "org.intellij.scala".toPlugin,
    packageMethod := PackagingMethod.Standalone(),
    Global / intellijAttachSources := true,
    Compile / javacOptions ++= "--release" :: "11" :: Nil
  )
  .withDependencies

lazy val `macro` = (project in file("macro"))
  .settings(commonSettings)
  .settings(moduleName := "functional-mock-macro")
  .withDependencies

lazy val `usage` = (project in file("usage"))
  .settings(commonSettings)
  .settings(
    moduleName := "usage",
    publish / skip := true
  )
  .withDependencies
  .settings(libraryDependencies += "org.qualiton" %% "functional-mock-macro" % "0.0.1-SNAPSHOT")
