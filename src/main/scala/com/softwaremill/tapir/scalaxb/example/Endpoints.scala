package com.softwaremill.tapir.scalaxb.example

import sttp.tapir.{PublicEndpoint, Schema, endpoint, query, stringBody}
import cats.effect.IO
import generated.Outer
import sttp.tapir.docs.apispec.DocsExtensionAttribute.RichSchema
import sttp.tapir.generic.auto._
import sttp.tapir.generic.Derived
import sttp.tapir.json.circe._
import io.circe.generic.auto._
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter

object Endpoints {

  // updated `Outer` schema with custom docs extension, so that example bodies at Swagger would have `xmlns`
  // attribute (look at "Prefixes and Namespaces" section at https://swagger.io/docs/specification/data-models/representing-xml/),
  // that is needed by scalaxb code to property decode given xml
  case class XmlNamespace(namespace: String)
  implicit val outerSchemaWithXmlNamspace: Schema[Outer] = implicitly[Derived[Schema[Outer]]].value
    .docsExtension("xml", XmlNamespace("http://www.example.com/innerouter"))

  import xml._
  // `label` is needed by scalaxb code to properly encode the top node of the xml
  implicit val label: XmlElementLabel = XmlElementLabel("outer")

  val xmlEndpoint: PublicEndpoint[Outer, Unit, Outer, Any] = endpoint.post
    .in("xml")
    .in(xmlBody[Outer])
    .out(xmlBody[Outer])

  val xmlServerEndpoint: ServerEndpoint[Any, IO] = xmlEndpoint.serverLogicSuccess(o =>
    IO.pure(o.copy(foo = o.foo.copy(a = o.foo.a * -1, b = !o.foo.b, c = o.foo.c.reverse), bar = o.bar.reverse))
  )

  val apiEndpoints: List[ServerEndpoint[Any, IO]] = List(xmlServerEndpoint)

  val docEndpoints: List[ServerEndpoint[Any, IO]] = SwaggerInterpreter()
    .fromServerEndpoints[IO](apiEndpoints, "tapir-scalaxb-example", "1.0.0")

  val all: List[ServerEndpoint[Any, IO]] = apiEndpoints ++ docEndpoints
}
