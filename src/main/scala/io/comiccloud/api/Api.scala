package io.comiccloud.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.Credentials
import io.comiccloud.datasource.{AbstractComicsDataSource, InMemoryComicsDataSource, InMemoryPasswordOauthDataSource}
import io.comiccloud.domain.User
import io.comiccloud.domain.request.OauthRequest

import scala.concurrent.Future
import scalaoauth2.provider._

trait Api extends ComicsApi with SeriesApi with UploadsApi with UsersApi with OauthApi{

  import scala.concurrent.ExecutionContext.Implicits.global
  //todo extract to application context

  val comicsDataSource: AbstractComicsDataSource = new InMemoryComicsDataSource()

  /*val tokenEndpoint = new TokenEndpoint {
    override val handlers = Map(
      "password" -> new Password())
  }*/

  /*val protectedResourceEndpoint = new ProtectedResource{

  }*/

  val oauthDataSource: DataHandler[User] = new InMemoryPasswordOauthDataSource

  /*def oauth2Authenticator(credentials: Credentials): Future[Option[AuthInfo[User]]] =
    credentials match {
      case Credentials.Provided(token) =>
        oauthDataSource.findAccessToken(token).flatMap {
          case Some(accessToken) => oauthDataSource.findAuthInfoByAccessToken(accessToken)
          case None => Future.successful(None)
        }
      case _ => Future.successful(None)
    }*/

  lazy val routes: Route = get {
    pathSingleSlash {
      complete {
        "Captain on the bridge!"
      }
    }
  } ~ oauthRoute ~ comicsRoute
    /*}~ authenticateOAuth2Async("???", oauth2Authenticator){ user =>
    comicsRoute(user) ~ seriesRoute ~ uploadsRoute ~ usersRoute
  } ~ oauthRoute ~
    //onComplete(tokenEndpoint.handleRequest(new AuthorizationRequest(Map(), requestToParams(oauthRequest)), oauthDataSource)) {*/
  /*} ~ onComplete(protectedResourceEndpoint.handleRequest(new ProtectedResourceRequest(Map(), Map()), oauthDataSource)).map{
    case Left(l) =>
  }*/


  /*private def requestToParams(request: OauthRequest): Map[String, Seq[String]] = {
    Map(
      "client_id" -> Seq(request.client_id),
      "client_secret" -> Seq(request.client_secret),
      "grant_type" -> Seq(request.grant_type),
      "username" -> Seq(request.username),
      "password" -> Seq(request.password)
    )
  }*/

}
