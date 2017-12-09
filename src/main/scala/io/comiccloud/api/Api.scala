package io.comiccloud.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.comiccloud.datasource.{AbstractComicsDataSource, InMemoryComicsDataSource}

trait Api extends ComicsApi with SeriesApi with UploadsApi with UsersApi {

  val comicsDataSource: AbstractComicsDataSource = new InMemoryComicsDataSource()

  lazy val routes: Route = get {
    pathSingleSlash {
      complete {
        "Captain on the bridge!"
      }
    }
  } ~ comicsRoute ~ seriesRoute ~ uploadsRoute ~ usersRoute

}
