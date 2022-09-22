package com.softwaremill.tapir.scalaxb.example

import com.softwaremill.tapir.scalaxb.example.Endpoints._
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client3.testing.SttpBackendStub
import sttp.client3.{UriContext, basicRequest}
import sttp.tapir.server.stub.TapirStubInterpreter

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import sttp.tapir.integ.cats.CatsMonadError

class EndpointsSpec extends AnyFlatSpec with Matchers with EitherValues {

  it should "return 'reversed' xml message" in {
    // given
    val backendStub = TapirStubInterpreter(SttpBackendStub(new CatsMonadError[IO]()))
      .whenServerEndpoint(xmlServerEndpoint)
      .thenRunLogic()
      .backend()

    // when
    val number = 42
    val boolean = true
    val innerText = "horses"
    val outerText = "cats"
    val response = basicRequest
      .post(uri"http://test.com/xml")
      .body(
        s"""<outer xmlns="http://www.example.com/innerouter"><foo><a>$number</a><b>$boolean</b><c>$innerText</c></foo><bar>$outerText</bar></outer>"""
      )
      .send(backendStub)

    // then
    response
      .map(
        _.body.value shouldBe s"""<outer xmlns="http://www.example.com/innerouter" xmlns:innerouter="http://www.example.com/innerouter" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><foo><a>${number * -1}</a><b>${!boolean}</b><c>${innerText.reverse}</c></foo><bar>${outerText.reverse}</bar></outer>"""
      )
      .unwrap
  }

  implicit class Unwrapper[T](t: IO[T]) {
    def unwrap: T = t.unsafeRunSync()
  }
}
