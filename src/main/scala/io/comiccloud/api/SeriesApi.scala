package io.comiccloud.api

import akka.http.scaladsl.server.Directives._
import io.comiccloud.domain.marshalling.JsonSerializers

trait SeriesApi extends JsonSerializers{

  val seriesRoute = path("series"){
    complete("series")
  } ~ path("series"/ JavaUUID) { id =>
    complete(id.toString)
  }

}
