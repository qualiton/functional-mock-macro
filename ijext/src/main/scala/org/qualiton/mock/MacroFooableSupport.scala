package org.qualiton.mock

import org.jetbrains.plugins.scala.lang.macros.evaluator.{ MacroContext, MacroImpl, ScalaMacroTypeable }
import org.jetbrains.plugins.scala.lang.psi.api.statements.ScFunction
import org.jetbrains.plugins.scala.lang.psi.impl.ScalaPsiElementFactory
import org.jetbrains.plugins.scala.lang.psi.types.{ ScParameterizedType, ScType }

class MacroFooableSupport extends ScalaMacroTypeable {
  override val boundMacro: Seq[MacroImpl] =
    MacroImpl("materialize", "org.qualiton.mock.Fooable") :: Nil

  override def checkMacro(macros: ScFunction, context: MacroContext): Option[ScType] = {
    val tpe = context.expectedType match {
      case Some(tp: ScParameterizedType) => tp.typeArguments.headOption.map(_.canonicalText).get
      case _                             => return None
    }

    println(s"<<<<<<<< $tpe")
    val txt = s"_root_.org.qualiton.mock.Fooable[$tpe] { def foo: $tpe }"
    ScalaPsiElementFactory.createTypeFromText(txt, context.place, null)
  }
}
