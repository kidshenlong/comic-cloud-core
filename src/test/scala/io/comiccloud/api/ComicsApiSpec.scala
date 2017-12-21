package io.comiccloud.api

import java.util.{Date, UUID}

import akka.http.scaladsl.model.headers.OAuth2BearerToken
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.model.ContentTypes.`application/json`
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.testkit.Specs2RouteTest
import io.comiccloud.datasource.AbstractComicsDataSource
import io.comiccloud.domain.marshalling.JsonSerializers
import io.comiccloud.domain.{Comic, User}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import scala.concurrent.Future
import scalaoauth2.provider._

class ComicsApiSpec extends Specification with Specs2RouteTest with Mockito with JsonSerializers{

  "The service" should {
    "return a speific comic for GET requests to the comics/{id} path" in {

      val mockedComicDataSource = mock[AbstractComicsDataSource]
      val mockedOauthDataSource = mock[DataHandler[User]]

      val authInfo = AuthInfo(User(UUID.randomUUID(), "user1", "password"), None, None, None)

      mockedOauthDataSource.findAccessToken(anyString) returns Future.successful(Some(AccessToken("token", None, None, None, new Date())))

      mockedOauthDataSource.findAuthInfoByAccessToken(any[AccessToken]) returns Future.successful(Some(authInfo))

      val comicUuid = UUID.randomUUID()
      val seriesUuid = UUID.randomUUID()

      val comic = Comic(comicUuid, 1, Map(), None, seriesUuid, Map())

      mockedComicDataSource.fetch(any[User], any[UUID]) returns Future.successful(Some(comic))


      val route = new ComicsApi {
        override val comicsDataSource: AbstractComicsDataSource = mockedComicDataSource
        override val oauthDataSource: DataHandler[User] = mockedOauthDataSource
      }

      val credential = OAuth2BearerToken("token")

      Get(s"/comics/$comicUuid") ~> addCredentials(credential) ~> Route.seal(route.comicsRoute) ~> check {
        status must be equalTo OK
        contentType must be equalTo `application/json`
        responseAs[Comic] shouldEqual comic
      }
    }
  }

}
