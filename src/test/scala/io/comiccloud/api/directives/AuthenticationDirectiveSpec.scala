package io.comiccloud.api.directives

import java.util.{Date, UUID}

import akka.http.scaladsl.model.headers.OAuth2BearerToken
import akka.http.scaladsl.server.directives.Credentials
import io.comiccloud.domain.User
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import scala.concurrent.{ExecutionContext, Future}
import scalaoauth2.provider._
import scala.concurrent._

class AuthenticationDirectiveSpec(implicit ec: ExecutionEnv) extends Specification with Mockito{


  /*val directives = new AuthenticationDirective {
    override val oauthDataSource: DataHandler[User] = new DataHandler[User] {
      override def findAuthInfoByAccessToken(accessToken: AccessToken): Future[Option[AuthInfo[User]]] =
        Future.successful(Some(authInfo))

      override def findAccessToken(token: String): Future[Option[AccessToken]] =
        Future.successful(Some(AccessToken("token", None, None, None, new Date())))

      override def findUser(maybeCredential: Option[ClientCredential], request: AuthorizationRequest): Future[Option[User]] = ???

      override def refreshAccessToken(authInfo: AuthInfo[User], refreshToken: String): Future[AccessToken] = ???

      override def getStoredAccessToken(authInfo: AuthInfo[User]): Future[Option[AccessToken]] = ???

      override def createAccessToken(authInfo: AuthInfo[User]): Future[AccessToken] = ???

      override def findAuthInfoByCode(code: String): Future[Option[AuthInfo[User]]] = ???

      override def validateClient(maybeCredential: Option[ClientCredential], request: AuthorizationRequest): Future[Boolean] = ???

      override def deleteAuthCode(code: String): Future[Unit] = ???

      override def findAuthInfoByRefreshToken(refreshToken: String): Future[Option[AuthInfo[User]]] = ???
    }
  }*/





  "AuthenticationDirective" should {
    "return nothing if no credentials are provided" in {
      val mockedOauthDataSource = mock[DataHandler[User]]

      val directives = new AuthenticationDirective{
        override val oauthDataSource: DataHandler[User] = mockedOauthDataSource
      }

      val credential = Credentials(None)
      directives.oauth2Authenticator(credential) must beNone.await
    }

    "return token if correct credentials are provided" in {
      val mockedOauthDataSource = mock[DataHandler[User]]

      val authInfo = AuthInfo(User(UUID.randomUUID(), "user1", "password"), None, None, None)

      mockedOauthDataSource.findAccessToken(anyString) returns Future.successful(Some(AccessToken("token", None, None, None, new Date())))

      mockedOauthDataSource.findAuthInfoByAccessToken(any[AccessToken]) returns Future.successful(Some(authInfo))

      val directives = new AuthenticationDirective{
        override val oauthDataSource: DataHandler[User] = mockedOauthDataSource
      }

      val credential = Credentials(Some(OAuth2BearerToken("token")))
      directives.oauth2Authenticator(credential) must beSome(authInfo).await
    }

    "return nothing if out of date credentials are provided" in {
      val mockedOauthDataSource = mock[DataHandler[User]]

      mockedOauthDataSource.findAccessToken(anyString) returns Future.successful(Some(AccessToken("token", None, None, Some(3600), new Date(2001))))

      val directives = new AuthenticationDirective{
        override val oauthDataSource: DataHandler[User] = mockedOauthDataSource
      }

      val credential = Credentials(Some(OAuth2BearerToken("token")))
      directives.oauth2Authenticator(credential) must beNone.await
    }

    "return nothing if invalid credentials are provided" in {
      val mockedOauthDataSource = mock[DataHandler[User]]

      mockedOauthDataSource.findAccessToken("invalid_token") returns Future.successful(None)

      val directives = new AuthenticationDirective{
        override val oauthDataSource: DataHandler[User] = mockedOauthDataSource
      }

      val credential = Credentials(Some(OAuth2BearerToken("invalid_token")))
      directives.oauth2Authenticator(credential) must beNone.await
    }

  }

}
