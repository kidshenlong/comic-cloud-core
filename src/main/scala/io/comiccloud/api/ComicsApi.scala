package io.comiccloud.api

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes.Success
import io.comiccloud.domain.marshalling.JsonSerializers
import akka.http.scaladsl.server.Directives._
import io.comiccloud.datasource.AbstractComicsDataSource
import io.comiccloud.domain.{Comic, User}
import io.comiccloud.domain.response.Response

import scala.concurrent.Future

trait ComicsApi extends JsonSerializers{

  val comicsDataSource: AbstractComicsDataSource

  val comicsRoute = path("comics"){
    get {
      onSuccess(comicsDataSource.comics(User(UUID.randomUUID(), "kidshenlong", "password"))) { results =>
        complete(Response(0, 0, 0, results))
      }
    } ~ post {
      entity(as[Seq[Comic]]) { comics =>

        import scala.concurrent.ExecutionContext.Implicits.global//todo obtain from application context
        onSuccess(Future.sequence(comics.map(comicsDataSource.createComic(_, User(UUID.randomUUID(), "kidshenlong", "password"))))){ res =>
          complete(Success)
        }
      }
    }
  } ~ path("comics"/ JavaUUID) { id =>
    onSuccess(comicsDataSource.comic(id, User(UUID.randomUUID(), "kidshenlong", "password"))){ result =>
      complete(result)
    }
  }

}
