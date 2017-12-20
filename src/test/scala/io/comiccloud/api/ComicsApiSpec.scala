package io.comiccloud.api

import java.util.UUID

import akka.http.scaladsl.testkit.Specs2RouteTest
import io.comiccloud.datasource.AbstractComicsDataSource
import io.comiccloud.domain.{Comic, User}
import org.specs2.mutable.Specification

import scala.concurrent.Future
import scalaoauth2.provider._

class ComicsApiSpec extends Specification with Specs2RouteTest {

  val route = new ComicsApi {

    override val comicsDataSource: AbstractComicsDataSource = new AbstractComicsDataSource {

      override def fetch(user: User, id: UUID): Future[Option[Comic]] = ???

      override def fetchAll(user: User, limit: Int, offset: Int): Future[Seq[Comic]] = ???

      override def create(user: User, comic: Comic): Future[Unit] = ???

      override def update(user: User, comic: Comic): Future[Unit] = ???

      override def delete(user: User, comic: Comic): Future[Unit] = ???

      override def fetchBySeries(user: User, seriesId: UUID, limit: Int, offset: Int): Future[Seq[Comic]] = ???
    }

    override val oauthDataSource: DataHandler[User] = new DataHandler[User] {override def findAuthInfoByAccessToken(accessToken: AccessToken): Future[Option[AuthInfo[User]]] = ???

      override def findAccessToken(token: String): Future[Option[AccessToken]] = ???

      override def findUser(maybeCredential: Option[ClientCredential], request: AuthorizationRequest): Future[Option[User]] = ???

      override def refreshAccessToken(authInfo: AuthInfo[User], refreshToken: String): Future[AccessToken] = ???

      override def getStoredAccessToken(authInfo: AuthInfo[User]): Future[Option[AccessToken]] = ???

      override def createAccessToken(authInfo: AuthInfo[User]): Future[AccessToken] = ???

      override def findAuthInfoByCode(code: String): Future[Option[AuthInfo[User]]] = ???

      override def validateClient(maybeCredential: Option[ClientCredential], request: AuthorizationRequest): Future[Boolean] = ???

      override def deleteAuthCode(code: String): Future[Unit] = ???

      override def findAuthInfoByRefreshToken(refreshToken: String): Future[Option[AuthInfo[User]]] = ???
    }

  }

  "The service" should {
    "return a greeting for GET requests to the root path" in {
      Get("/comics") ~> route.comicsRoute ~> check {
        responseAs[String] shouldEqual "Captain on the bridge!"
      }
    }
  }

}
