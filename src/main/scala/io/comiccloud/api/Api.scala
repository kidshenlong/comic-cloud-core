package io.comiccloud.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

trait Api extends ComicsApi with SeriesApi with UploadsApi with UsersApi {

  lazy val routes: Route = get {
    pathSingleSlash {
      complete {
        "Captain on the bridge!"
      }
    }
  } ~ comicsRoute ~ seriesRoute ~ uploadsRoute ~ usersRoute

}
