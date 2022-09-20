package com.softwaremill.tapir.scalaxb.example.xml

import generated.`package`.defaultScope
import scalaxb.XMLFormat
import scalaxb.`package`.{fromXML, toXML}
import sttp.tapir.Codec.XmlCodec
import sttp.tapir.DecodeResult.{Error, Value}
import sttp.tapir.{Codec, EndpointIO, Schema, stringBodyUtf8AnyFormat}

import scala.xml.{NodeSeq, XML}

trait TapirXmlScalaxb {
  case class XmlElementLabel(label: String)

  def xmlBody[T: XMLFormat: Schema](implicit l: XmlElementLabel): EndpointIO.Body[String, T] = stringBodyUtf8AnyFormat(scalaxbCodec[T])

  implicit def scalaxbCodec[T: XMLFormat: Schema](implicit label: XmlElementLabel): XmlCodec[T] = {
    Codec.xml((s: String) =>
      try {
        Value(fromXML[T](XML.loadString(s)))
      } catch {
        case e: Exception => Error(s, e)
      }
    )((t: T) => {
      val nodeSeq: NodeSeq = toXML[T](obj = t, elementLabel = label.label, scope = defaultScope)
      nodeSeq.toString()
    })
  }
}
