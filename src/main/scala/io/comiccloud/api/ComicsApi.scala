package io.comiccloud.api

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes.Success
import io.comiccloud.domain.marshalling.JsonSerializers
import akka.http.scaladsl.server.Directives._
import io.comiccloud.datasource.AbstractComicsDataSource
import io.comiccloud.domain.{Comic, User}
import io.comiccloud.domain.response.Response

import scala.concurrent.Future
import scalaoauth2.provider.{AuthInfo, DataHandler, ProtectedResource, ProtectedResourceRequest}

trait ComicsApi extends JsonSerializers with ProtectedResource{

  val comicsDataSource: AbstractComicsDataSource

  val oauthDataSource: DataHandler[User]

  import scala.concurrent.ExecutionContext.Implicits.global
  //todo extract to application context

  /*def comicsRoute(authInfo: AuthInfo[User]) = path("comics"){
    get {
      onSuccess(comicsDataSource.comics(authInfo.user)) { results =>
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
  }*/


  lazy val comicsRoute =

        path("comics") {
          onSuccess(handleRequest(new ProtectedResourceRequest(Map.empty, Map("access_token" -> Seq("lulz"))), oauthDataSource)) {
            case Left(e) => throw e
            case Right(authInfo) =>
              get {
                onSuccess(comicsDataSource.comics(authInfo.user)) { results =>
                  complete(Response(0, 0, 0, results))
                } ~ path(JavaUUID) { id =>
                  onSuccess(comicsDataSource.comic(id, User(UUID.randomUUID(), "kidshenlong", "password"))) { result =>
                    complete(result)
                  }
                }
              } ~ post {
                entity(as[Seq[Comic]]) { comics =>
                  import scala.concurrent.ExecutionContext.Implicits.global
                  //todo obtain from application context
                  onSuccess(Future.sequence(comics.map(comicsDataSource.createComic(_, User(UUID.randomUUID(), "kidshenlong", "password"))))) { res =>
                    complete(Success)
                  }
                }
              }
          }
        }



}
