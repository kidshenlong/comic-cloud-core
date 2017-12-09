package io.comiccloud.domain.marshalling

import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, Formats, jackson}

trait JsonSerializers extends Json4sSupport{

  implicit val serialization = jackson.Serialization
  implicit val jsonFormats: Formats = DefaultFormats.preservingEmptyValues ++ org.json4s.ext.JavaTypesSerializers.all

}
