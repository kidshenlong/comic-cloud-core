package io.comiccloud.api

import akka.http.scaladsl.server.Directives._
import io.comiccloud.api.directives.AuthenticationDirective
import io.comiccloud.domain.marshalling.JsonSerializers

trait UsersApi extends JsonSerializers with AuthenticationDirective{

  val usersRoute =
    authenticateOAuth2Async("???", oauth2Authenticator) { authInfo =>
      path("users" / "me") {
        complete("user")
      }
    }

}
