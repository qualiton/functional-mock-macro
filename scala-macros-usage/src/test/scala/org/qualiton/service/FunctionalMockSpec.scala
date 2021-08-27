package org.qualiton.service

import cats.effect.IO
import cats.implicits._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class FunctionalMockSpec extends AnyFreeSpec with Matchers {

  "FunctionalMock" - {
    "should generate valid mock" in {
      val test: IO[Unit] = for {
        mockService <- MockServiceManual()
        _ <- mockService.whenService2(_ => "OK".pure[IO])
        result <- mockService.service2("test")
        _ <- IO(result should ===("OK"))
      } yield ()

      test.unsafeRunSync()
    }
  }
}
