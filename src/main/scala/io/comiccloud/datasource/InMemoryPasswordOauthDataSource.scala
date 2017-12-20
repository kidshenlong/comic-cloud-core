package io.comiccloud.datasource

import java.util.{Date, UUID}

import io.comiccloud.config.Config
import io.comiccloud.domain.User

import scala.collection.mutable
import scala.concurrent.Future
import scala.util.Random
import scalaoauth2.provider._
import io.comiccloud.datasource.OauthMockData._

class InMemoryPasswordOauthDataSource extends DataHandler[User] with Config {

  override def validateClient(maybeCredential: Option[ClientCredential], request: AuthorizationRequest): Future[Boolean] = {
    Future.successful{
      maybeCredential match {
        case Some(ClientCredential(clientId, Some(clientSecret))) => clientCredentialsStore.get(clientId).contains(clientSecret)
        case _ => false
      }
    }
  }

  override def findUser(maybeCredential: Option[ClientCredential], request: AuthorizationRequest): Future[Option[User]] = Future.successful{
    userStore.find(user =>
      user.username == request.asInstanceOf[PasswordRequest].username
        && user.hashedPassword == request.asInstanceOf[PasswordRequest].password
    )
  }

  override def createAccessToken(authInfo: AuthInfo[User]): Future[AccessToken] = {
    val accessToken = AccessToken(Random.alphanumeric.take(10).mkString, Some(Random.alphanumeric.take(10).mkString), authInfo.scope, Some(accessTokenLifeInSecond), new Date)

    accessTokensStore.get(authInfo.user.id) match {
      case Some(accessTokens) => accessTokens += AccessTokenInformation(accessToken, authInfo.clientId.get)
      case None => accessTokensStore += authInfo.user.id -> mutable.Buffer(AccessTokenInformation(accessToken, authInfo.clientId.get))
    }

    Future.successful(accessToken)
  }

  override def getStoredAccessToken(authInfo: AuthInfo[User]): Future[Option[AccessToken]] = {
    Future.successful{
      accessTokensStore.get(authInfo.user.id)
        .map(accessTokens => accessTokens.maxBy(_.accessToken.createdAt))
        .map( accessTokenInformation =>
          AccessToken(
            accessTokenInformation.accessToken.token,
            accessTokenInformation.accessToken.refreshToken,
            accessTokenInformation.accessToken.scope,
            accessTokenInformation.accessToken.lifeSeconds,
            accessTokenInformation.accessToken.createdAt,
            accessTokenInformation.accessToken.params
          )
        )// recreate access token https://github.com/nulab/scala-oauth2-provider/issues/118
    }
  }

  override def refreshAccessToken(authInfo: AuthInfo[User], refreshToken: String): Future[AccessToken] = {
    println("refreshing")
    createAccessToken(authInfo)
  }

  override def findAuthInfoByCode(code: String): Future[Option[AuthInfo[User]]] = ??? //don't need to implement for this grant

  override def deleteAuthCode(code: String): Future[Unit] = ??? //don't need to implement for this grant

  override def findAuthInfoByRefreshToken(refreshToken: String): Future[Option[AuthInfo[User]]] = ??? //don't need to implement for this grant

  override def findAuthInfoByAccessToken(accessToken: AccessToken): Future[Option[AuthInfo[User]]] = {
    Future.successful{
      accessTokensStore.find{ case (_, r) =>
        r.exists(_.accessToken.token == accessToken.token)
      }.flatMap{
        case (userId, accessTokensInformation) => {
          userStore.find(_.id == userId).flatMap{ user =>
            accessTokensInformation.find(_.accessToken.token == accessToken.token).map{ accessTokenInformation =>
              AuthInfo(user, Some(accessTokenInformation.clientId), None, None)
            }
          }
        }
      }
    }
  }

  override def findAccessToken(token: String): Future[Option[AccessToken]] = {
    Future.successful{
      accessTokensStore.find{ case (_, r) =>
        r.exists(_.accessToken.token == token)
      }.flatMap(_._2.find(_.accessToken.token == token))
        .map(accessTokenInformation =>
          AccessToken(
            accessTokenInformation.accessToken.token,
            accessTokenInformation.accessToken.refreshToken,
            accessTokenInformation.accessToken.scope,
            accessTokenInformation.accessToken.lifeSeconds,
            accessTokenInformation.accessToken.createdAt,
            accessTokenInformation.accessToken.params
          )
        )// recreate access token https://github.com/nulab/scala-oauth2-provider/issues/118
    }
  }
}
