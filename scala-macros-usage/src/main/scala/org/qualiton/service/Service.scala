package org.qualiton.service

trait Service[F[_]] {
  def service1(i: Int, s: String): F[Long]

  def service2(i: String): F[String]
}
