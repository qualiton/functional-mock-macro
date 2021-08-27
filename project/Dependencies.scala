import sbt.Keys.libraryDependencies
import sbt._

object Dependencies extends AutoPlugin {
  object autoImport {
    implicit class DependencySettings(val project: Project) extends AnyVal {
      def withDependencies: Project =
        project
          .settings(libraryDependencies ++= (prodDependencies ++ testDependencies))
    }
  }

  lazy val prodDependencies = List(
    "org.scala-lang" % "scala-reflect" % "2.13.4",
    "org.typelevel" %% "cats-core"     % "2.6.1",
    "org.typelevel" %% "cats-effect"   % "3.2.2"
  )

  lazy val testDependencies = List(
    // Test
    "com.ironcorelabs"     %% "cats-scalatest"      % "3.1.1",
    "org.scalatest"        %% "scalatest"           % "3.2.9",
    "com.github.cb372"     %% "cats-retry"          % "3.0.0",
    "com.github.chocpanda" %% "scalacheck-magnolia" % "0.6.0"
  ).map(_ % Test)
}
