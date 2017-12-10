package io.comiccloud.domain.marshalling

import java.text.DateFormat
import java.util.Date

import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.JsonAST.{JNull, JString}
import org.json4s.{CustomSerializer, DefaultFormats, Formats, jackson}

trait JsonSerializers extends Json4sSupport{

  implicit val serialization = jackson.Serialization
  implicit val jsonFormats: Formats = DefaultFormats ++ org.json4s.ext.JavaTypesSerializers.all ++ org.json4s.ext.JodaTimeSerializers.all

}
