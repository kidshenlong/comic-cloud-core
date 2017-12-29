package io.comiccloud.api

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.testkit.Specs2RouteTest
import io.comiccloud.datasource.AbstractComicsDataSource
import akka.http.scaladsl.model.ContentTypes.`application/json`
import io.comiccloud.domain.User
import io.comiccloud.domain.request.OauthRequest
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}

import scalaoauth2.provider.DataHandler

class OauthApiSpec extends Specification with Specs2RouteTest with Mockito{

  "Oauth Route" should {
    "return token if correct credentials are provided" in {

      val mockedOauthDataSource = mock[DataHandler[User]]

      val route = new OauthApi {
        override val oauthDataSource: DataHandler[User] = mockedOauthDataSource
      }

      implicit val formats = Serialization.formats(NoTypeHints)

      val request = OauthRequest(
        client_id = "test_client",
        client_secret = "test_secret",
        grant_type = "password",
        username = "test_user",
        password = "test_password"
      )

      val ser = write(request)

      Post("/oauth/token", HttpEntity(`application/json`, ser)) ~> route.oauthRoute ~> check {
        responseAs[String] shouldEqual "Captain on the bridge!"
      }
    }
  }

}
