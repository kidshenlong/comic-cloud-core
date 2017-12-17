package io.comiccloud.api

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes.Success
import io.comiccloud.domain.marshalling.JsonSerializers
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.Credentials
import io.comiccloud.api.directives.AuthenticationDirective
import io.comiccloud.datasource.AbstractComicsDataSource
import io.comiccloud.domain.{Comic, User}
import io.comiccloud.domain.response.Response

import scala.concurrent.Future
import scalaoauth2.provider.{AuthInfo, DataHandler, ProtectedResource, ProtectedResourceRequest}

trait ComicsApi extends JsonSerializers with AuthenticationDirective {

  val comicsDataSource: AbstractComicsDataSource

  import scala.concurrent.ExecutionContext.Implicits.global
  //todo extract to application context

  val comicsRoute =

    authenticateOAuth2Async("???", oauth2Authenticator){ authInfo =>
      path("comics") {
        /*get { //todo getting all comics probably doesn't make sense
          onSuccess(comicsDataSource.comics(authInfo.user)) { results =>
            complete(Response(0, 0, 0, results))
          }
        } ~ */post {
          entity(as[Seq[Comic]]) { comics =>
            onSuccess(Future.sequence(comics.map((comic: Comic) => comicsDataSource.create(authInfo.user, comic)))) { res =>
              complete(Success)
            }
          }
        }
      } ~ path("comics" / JavaUUID) { id =>
        onSuccess(comicsDataSource.fetch(authInfo.user, id)) { result =>
          complete(result)
        }
      }
    }

}
