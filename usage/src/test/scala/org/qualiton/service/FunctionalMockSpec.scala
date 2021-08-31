package org.qualiton.service

import cats.data.Chain
import cats.effect.IO
import cats.implicits._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class FunctionalMockSpec extends AnyFreeSpec with Matchers {
  "FunctionalMock" - {
    "should generate valid mock" in {
      val test: IO[Unit] = for {
        mockService <- MockServiceManual() // Will be changed to MockService after the companion is generated by the macro
        _           <- mockService.whenService2((x: String) => ("OK", x.length).pure[IO])
        result      <- mockService.service2("test")
        calls       <- mockService.service2HistoryRef.get
        _ <- IO {
          MockService("printed from generated companion") //Testing generated companion
          result should === (("OK", 4))
          calls should === (Chain(("test", Right(("OK", 4)))))
        }
        _ <- mockService.clean()
      } yield ()

      test.unsafeRunSync()
    }
  }
}