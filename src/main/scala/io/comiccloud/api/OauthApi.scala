package io.comiccloud.api

import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.comiccloud.datasource.{AbstractComicsDataSource, InMemoryOauthDataSource}
import io.comiccloud.domain.User
import io.comiccloud.domain.marshalling.JsonSerializers
import io.comiccloud.domain.request.OauthRequest

import scala.util.{Failure, Success}
import scalaoauth2.provider.{AuthorizationRequest, DataHandler, Password, TokenEndpoint}

trait OauthApi extends JsonSerializers{

  val tokenEndpoint: TokenEndpoint

  val oauthDataSource: DataHandler[User]

  import scala.concurrent.ExecutionContext.Implicits.global
  //todo extract to application context

  val oauthRoute:Route = path("oauth" / "token") {
    post {
      entity(as[OauthRequest]) { oauthRequest =>
        onComplete(tokenEndpoint.handleRequest(new AuthorizationRequest(Map(), requestToParams(oauthRequest)), oauthDataSource)) {
          case Success(maybeGrantResponse) =>
            maybeGrantResponse.fold(
              oauthError => complete(oauthError),
              grantResult => complete(grantResult.toString)
            )
          case Failure(ex) => complete(ex)//complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
        }
      }
    }
  }



  def requestToParams(request: OauthRequest): Map[String, Seq[String]] = {
    Map(
      "client_id" -> Seq(request.client_id),
      "client_secret" -> Seq(request.client_secret),
      "grant_type" -> Seq(request.grant_type),
      "username" -> Seq(request.username),
      "password" -> Seq(request.password)
    )
  }

}
