package io.comiccloud.api

import java.util.Date

import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.comiccloud.datasource.{AbstractComicsDataSource, InMemoryPasswordOauthDataSource}
import io.comiccloud.domain.User
import io.comiccloud.domain.marshalling.JsonSerializers
import io.comiccloud.domain.request.OauthRequest
import io.comiccloud.domain.response.OauthResponse

import scala.util.{Failure, Success}
import scalaoauth2.provider.{AuthorizationRequest, DataHandler, Password, TokenEndpoint}

trait OauthApi extends JsonSerializers with TokenEndpoint{

  //val tokenEndpoint: TokenEndpoint

  override val handlers = Map(
    "password" -> new Password()
  )

  val oauthDataSource: DataHandler[User]

  import scala.concurrent.ExecutionContext.Implicits.global
  //todo extract to application context

  val oauthRoute:Route = path("oauth" / "token") {
    post {
      entity(as[OauthRequest]) { oauthRequest =>
        onComplete(handleRequest(new AuthorizationRequest(Map(), requestToParams(oauthRequest)), oauthDataSource)) {
          case Success(maybeGrantResponse) =>
            maybeGrantResponse.fold(
              oauthError => complete(oauthError),
              grantResult => {
                val response = OauthResponse(grantResult.accessToken, grantResult.refreshToken.get, grantResult.expiresIn.get)

                complete(response)
              }
            )
          case Failure(ex) => complete(ex)//complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
        }
      }
    }
  }



  private def requestToParams(request: OauthRequest): Map[String, Seq[String]] = {
    Map(
      "client_id" -> Seq(request.client_id),
      "client_secret" -> Seq(request.client_secret),
      "grant_type" -> Seq(request.grant_type),
      "username" -> Seq(request.username),
      "password" -> Seq(request.password)
    )
  }

}
