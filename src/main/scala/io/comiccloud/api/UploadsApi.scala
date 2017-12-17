package io.comiccloud.api

import akka.http.scaladsl.server.Directives._
import io.comiccloud.api.directives.AuthenticationDirective
import io.comiccloud.domain.marshalling.JsonSerializers

trait UploadsApi extends JsonSerializers with AuthenticationDirective{

  val uploadsRoute =
    authenticateOAuth2Async("???", oauth2Authenticator) { authInfo =>
      path("uploads") {
        complete("uploads")
      } ~ path("uploads" / JavaUUID) { id =>
        complete(id.toString)
      }
    }
}
