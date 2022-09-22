package com.softwaremill.tapir.scalaxb.example

import generated.{Inner, Outer}
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import sttp.tapir.DecodeResult
import sttp.tapir.generic.auto.schemaForCaseClass

import xml._

class TapirXmlScalaxbTest extends AnyFlatSpecLike with Matchers {

  import generated.Generated_OuterFormat

  implicit val label: XmlElementLabel = XmlElementLabel("outer")

  it should "encode given class" in {
    val body = Outer(Inner(42, b = true, "horses"), "cats")
    val expected =
      s"""<${label.label} xmlns="http://www.example.com/innerouter" xmlns:innerouter="http://www.example.com/innerouter" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><foo><a>42</a><b>true</b><c>horses</c></foo><bar>cats</bar></${label.label}>"""

    val codec = scalaxbCodec[Outer]
    val result = codec.encode(body)

    result shouldBe expected
  }

  it should "decode given xml text" in {
    val body =
      s"""<${label.label} xmlns="http://www.example.com/innerouter"><foo><a>42</a><b>true</b><c>horses</c></foo><bar>cats</bar></${label.label}>"""
    val expected = DecodeResult.Value(Outer(Inner(42, b = true, "horses"), "cats"))

    val codec = scalaxbCodec[Outer]
    val result = codec.decode(body)

    result shouldBe expected
  }

  it should "return an error on object decode failure" in {
    val input = """not valid xml"""

    val codec = scalaxbCodec[Outer]
    val actual = codec.decode(input)

    actual shouldBe a[DecodeResult.Error]
  }
}
