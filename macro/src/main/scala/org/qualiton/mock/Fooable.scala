package org.qualiton.mock

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

trait Fooable[T]
object Fooable {
  implicit def materialize[T]: Fooable[T] = macro impl[T]

  def impl[T : c.WeakTypeTag](c: whitebox.Context): c.Tree = {
    import c.universe._
    val tpe = c.weakTypeOf[T]
    println(s"===>>> $tpe")
    q"new _root_.org.qualiton.mock.Fooable[$tpe] { def foo: $tpe = ??? }"
  }
}
