package io.comiccloud.api

import akka.http.scaladsl.server.Directives._
import io.comiccloud.domain.marshalling.JsonSerializers

trait UploadsApi extends JsonSerializers{

  val uploadsRoute = path("uploads"){
    complete("uploads")
  } ~ path("uploads"/ JavaUUID) { id =>
    complete(id.toString)
  }

}
