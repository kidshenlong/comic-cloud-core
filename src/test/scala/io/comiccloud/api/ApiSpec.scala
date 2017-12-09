package io.comiccloud.api

import akka.http.scaladsl.testkit.Specs2RouteTest
import org.specs2.mutable.Specification

class ApiSpec extends Specification with Specs2RouteTest with Api {

  "The service" should {
    "return a greeting for GET requests to the root path" in {
      Get() ~> routes ~> check {
        responseAs[String] shouldEqual "Captain on the bridge!"
      }
    }
  }

}
