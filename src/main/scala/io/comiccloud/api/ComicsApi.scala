package io.comiccloud.api

import io.comiccloud.domain.marshalling.JsonSerializers
import akka.http.scaladsl.server.Directives._

trait ComicsApi extends JsonSerializers{

  val comicsRoute = path("comics"){
    complete("comics")
  } ~ path("comics"/ JavaUUID) { id =>
    complete(id.toString)
  }

}
