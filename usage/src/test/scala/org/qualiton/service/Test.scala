package org.qualiton.service

import org.qualiton.mock._

@MySampleAnnotation
object Test {
  val a: String             = genFoo("123").foo
  val b: TestClassA[String] = MyApplyDynamicMacro.foo("Int")
//
  this.myNewCoolMethod(323)
}

object Main extends App {
  println(Test.myNewCoolMethod(323))
//  println(Test.a)
  println(Test.b)
}
