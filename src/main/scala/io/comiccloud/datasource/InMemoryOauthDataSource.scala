package io.comiccloud.datasource

import java.util.{Date, UUID}

import io.comiccloud.domain.User

import scala.collection.mutable
import scala.concurrent.Future
import scala.util.Random
import scalaoauth2.provider._

class InMemoryOauthDataSource extends DataHandler[User] {

  private val clientCredentialsStore: Map[String, String] = Map("client_id_1" -> "client_secret_1")
  private val userStore: Seq[User] = Seq(
    User(UUID.randomUUID(), "kidshenlong", "password"),
    User(UUID.randomUUID(), "other_user", "password")
  )
  private var accessTokensStore: mutable.Map[UUID, mutable.Buffer[AccessToken]] = mutable.Map()

  override def validateClient(maybeCredential: Option[ClientCredential], request: AuthorizationRequest): Future[Boolean] = {
    Future.successful{
      maybeCredential match {
        case None => false
        case Some(ClientCredential(clientId, Some(clientSecret))) => clientCredentialsStore.get(clientId).contains(clientSecret)
      }
    }
  }

  override def findUser(maybeCredential: Option[ClientCredential], request: AuthorizationRequest): Future[Option[User]] = Future.successful{
    userStore.find(user =>
      user.name == request.asInstanceOf[PasswordRequest].username
        && user.hashedPassword == request.asInstanceOf[PasswordRequest].password
    )
  }


  override def createAccessToken(authInfo: AuthInfo[User]): Future[AccessToken] = {
    val accessToken = AccessToken(Random.alphanumeric.take(10).mkString, None, None, Some(3600), new Date)

    accessTokensStore.get(authInfo.user.id) match {
      case Some(accessTokens) => accessTokens += accessToken
      case None => accessTokensStore += authInfo.user.id -> mutable.Buffer(accessToken)
    }

    Future.successful(accessToken)
  }

  override def getStoredAccessToken(authInfo: AuthInfo[User]): Future[Option[AccessToken]] = {
    Future.successful{
      accessTokensStore.get(authInfo.user.id)
        .map(accessTokens => accessTokens.minBy(_.createdAt))
        .map( accessToken =>
          AccessToken(
            accessToken.token,
            accessToken.refreshToken,
            accessToken.scope,
            accessToken.lifeSeconds,
            accessToken.createdAt,
            accessToken.params
          )
        )// recreate access token https://github.com/nulab/scala-oauth2-provider/issues/118
    }
  }

  override def refreshAccessToken(authInfo: AuthInfo[User], refreshToken: String): Future[AccessToken] = ???

  override def findAuthInfoByCode(code: String): Future[Option[AuthInfo[User]]] = ???

  override def deleteAuthCode(code: String): Future[Unit] = ???

  override def findAuthInfoByRefreshToken(refreshToken: String): Future[Option[AuthInfo[User]]] = ???

  override def findAuthInfoByAccessToken(accessToken: AccessToken): Future[Option[AuthInfo[User]]] = ???

  override def findAccessToken(token: String): Future[Option[AccessToken]] = ???
}
