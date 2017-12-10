package io.comiccloud.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.comiccloud.datasource.{AbstractComicsDataSource, InMemoryComicsDataSource, InMemoryOauthDataSource}
import io.comiccloud.domain.User

import scalaoauth2.provider.{DataHandler, Password, TokenEndpoint}

trait Api extends ComicsApi with SeriesApi with UploadsApi with UsersApi with OauthApi{

  val comicsDataSource: AbstractComicsDataSource = new InMemoryComicsDataSource()

  val tokenEndpoint = new TokenEndpoint {
    override val handlers = Map(
      "password" -> new Password())
  }

  val oauthDataSource: DataHandler[User] = new InMemoryOauthDataSource

  lazy val routes: Route = get {
    pathSingleSlash {
      complete {
        "Captain on the bridge!"
      }
    }
  } ~ comicsRoute ~ seriesRoute ~ uploadsRoute ~ usersRoute ~ oauthRoute

}
