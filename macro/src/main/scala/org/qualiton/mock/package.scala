package org.qualiton

package object mock {
  def genFoo[T](x: T)(implicit s: Fooable[T]): s.type = s
}
