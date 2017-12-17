package io.comiccloud.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.Credentials
import io.comiccloud.datasource._
import io.comiccloud.domain.User
import io.comiccloud.domain.request.OauthRequest

import scala.concurrent.Future
import scalaoauth2.provider._

trait Api extends ComicsApi with SeriesApi with UploadsApi with UsersApi with OauthApi{

  import scala.concurrent.ExecutionContext.Implicits.global
  //todo extract to application context

  val comicsDataSource: AbstractComicsDataSource = new InMemoryComicsDataSource()
  val seriesDataSource: AbstractSeriesDataSource = new InMemorySeriesDataSource()


  val oauthDataSource: DataHandler[User] = new InMemoryPasswordOauthDataSource

  lazy val routes: Route = get {
    pathSingleSlash {
      complete {
        "Captain on the bridge!"
      }
    }
  } ~ oauthRoute ~ comicsRoute ~ seriesRoute ~ uploadsRoute ~ usersRoute

}
