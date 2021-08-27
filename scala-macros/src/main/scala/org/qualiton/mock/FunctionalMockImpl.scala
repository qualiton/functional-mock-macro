package org.qualiton.mock

import scala.annotation.StaticAnnotation
import scala.reflect.macros.whitebox

class FunctionalMock extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro FunctionalMockImpl.impl
}

@annotation.nowarn
object FunctionalMockImpl {

  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    def extractParameterTypeForMockMethod(param: Tree): Tree = param match {
      case q"$_ val $_: $tpt = $_" => tpt
    }

    def generateAbstractMockMethod(tree: Tree): Tree = tree match {
      case q"$_ def $tname[..$tparams](...$paramss): $tpt" =>
        val newMockMethodName = TermName(s"when${Character.toUpperCase(tname.toString().charAt(0)) + tname.toString().substring(1)}")

        val mockParams =
          paramss.map(p => p.map(p => extractParameterTypeForMockMethod(p))).map(p => q"val mockF: ..$p => $tpt")
        q"def $newMockMethodName[..$tparams](...$mockParams): F[Unit]"
    }

    def generateMockHistoryRef(tree: Tree): Tree = tree match {
      case q"$_ def $tname[..$_](...$paramss): $tpt" =>
        val newValRefName = TermName(s"${tname.toString()}HistoryRef")

        val mockType =
          paramss.map(p => p.map(p => extractParameterTypeForMockMethod(p))).map(p => q"Ref[F, Chain[((..$p), Either[Throwable, $tpt])]]")
        val t = q"val $newValRefName: ${mockType.head}"

        println(t)
        t
    }

    val result: c.universe.Tree = annottees.map(_.tree).toList match {
      case q"$mods trait $tpname[..$tparams] extends { ..$earlydefns } with ..$parents { $self => ..$stats }" :: Nil =>
        val defClean       = q"def clean(): F[Unit]"
        val defMocks       = stats.collect(s => generateAbstractMockMethod(s))
        val valHistoryRefs = stats.collect(s => generateMockHistoryRef(s))

        val newStats       = defClean :: valHistoryRefs ::: defMocks ::: stats.toList
//        println(newStats)

        val w = q"""
           $mods trait $tpname[..$tparams] extends { ..$earlydefns } with ..$parents {
            $self =>

            import cats.data.Chain
            import cats.effect.concurrent.Ref

            ..$newStats
         }"""

        println(w)
        w
      case x =>
        println(x)
        c.abort(c.enclosingPosition, "FunctionalMock should be on traits")
    }
    c.Expr[Any](result)
  }
}
