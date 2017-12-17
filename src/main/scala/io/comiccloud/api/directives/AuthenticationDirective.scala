package io.comiccloud.api.directives

import akka.http.scaladsl.server.directives.Credentials
import io.comiccloud.domain.User

import scala.concurrent.Future
import scalaoauth2.provider.{AuthInfo, DataHandler}

trait AuthenticationDirective {

  val oauthDataSource: DataHandler[User]

  import scala.concurrent.ExecutionContext.Implicits.global
  //todo extract to application context

  def oauth2Authenticator(credentials: Credentials): Future[Option[AuthInfo[User]]] =
    credentials match {
      case Credentials.Provided(token) =>
        oauthDataSource.findAccessToken(token).flatMap {
          case Some(accessToken) if !accessToken.isExpired => oauthDataSource.findAuthInfoByAccessToken(accessToken)
          case _ => Future.successful(None)
        }
      case _ => Future.successful(None)
    }
}
