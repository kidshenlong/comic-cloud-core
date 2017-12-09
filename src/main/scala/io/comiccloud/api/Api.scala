package io.comiccloud.api

import akka.http.scaladsl.server.Directives.{complete, get, pathSingleSlash}
import akka.http.scaladsl.server.Route

trait Api {

  lazy val routes: Route = get {
    pathSingleSlash {
      complete {
        "Captain on the bridge!"
      }
    }
  }

}
