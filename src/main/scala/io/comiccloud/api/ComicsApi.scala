package io.comiccloud.api

import io.comiccloud.domain.marshalling.JsonSerializers
import akka.http.scaladsl.server.Directives._
import io.comiccloud.datasource.AbstractComicsDataSource
import io.comiccloud.domain.User
import io.comiccloud.domain.response.Response

trait ComicsApi extends JsonSerializers{

  val comicsDataSource: AbstractComicsDataSource

  val comicsRoute = path("comics"){
    onSuccess(comicsDataSource.comics(User())) { results =>
      complete(Response(0, 0, 0, results))
    }
  } ~ path("comics"/ JavaUUID) { id =>
    onSuccess(comicsDataSource.comic(id, User())){ result =>
      complete(result)
    }
  }

}
