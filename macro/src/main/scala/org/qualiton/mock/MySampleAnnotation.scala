package org.qualiton.mock

import scala.annotation.{ StaticAnnotation, compileTimeOnly }
import scala.reflect.macros.whitebox

@compileTimeOnly("MySampleAnnotation is available only during compile-time")
class MySampleAnnotation extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro MySampleAnnotationImpl.impl
}

@annotation.nowarn
object MySampleAnnotationImpl {

  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    val result = annottees.map(_.tree).toList match {
      case q"$mods object $tname extends { ..$earlydefns } with ..$parents { $self => ..$body }" :: Nil =>
        q"""           
           $mods object $tname extends { ..$earlydefns } with ..$parents { 
            $self =>

            def myNewCoolMethod(a: Int): String = "Test" + a

            ..$body
         }"""
      case _ => c.abort(c.enclosingPosition, "MySampleAnnotationImpl should be on object")
    }

    println(result)
    c.Expr[Any](result)
  }
}
