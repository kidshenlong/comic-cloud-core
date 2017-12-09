package io.comiccloud.api

import akka.http.scaladsl.server.Directives._
import io.comiccloud.domain.marshalling.JsonSerializers

trait UsersApi extends JsonSerializers{

  val usersRoute = path("users" / "me"){
    complete("user")
  }

}
