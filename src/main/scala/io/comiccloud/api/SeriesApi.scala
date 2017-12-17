package io.comiccloud.api

import akka.http.scaladsl.model.StatusCodes.Success
import akka.http.scaladsl.server.Directives._
import io.comiccloud.api.directives.AuthenticationDirective
import io.comiccloud.datasource.{AbstractComicsDataSource, AbstractSeriesDataSource}
import io.comiccloud.domain.{Comic, Series}
import io.comiccloud.domain.marshalling.JsonSerializers
import io.comiccloud.domain.response.Response

import scala.concurrent.Future

trait SeriesApi extends JsonSerializers with AuthenticationDirective {

  val seriesDataSource: AbstractSeriesDataSource
  val comicsDataSource: AbstractComicsDataSource

  import scala.concurrent.ExecutionContext.Implicits.global
  //todo extract to application context

  val seriesRoute =
    authenticateOAuth2Async("???", oauth2Authenticator) { authInfo =>
      parameters('limit ? 20, 'offset ? 0) { (limit, offset) =>
        path("series") {
          get { //todo getting all comics probably doesn't make sense
            onSuccess(seriesDataSource.fetchAll(authInfo.user, limit, offset)) { results =>
              complete(Response(0, 0, 0, results))
            }
          } ~ post {
            entity(as[Seq[Series]]) { series =>
              onSuccess(Future.sequence(series.map((series: Series) => seriesDataSource.create(authInfo.user, series)))) { res =>
                complete(Success)
              }
            }
          }
        } ~ path("series" / JavaUUID) { id =>
          onSuccess(seriesDataSource.fetch(authInfo.user, id)) { result =>
            complete(result)
          }
        } ~ path("series" / JavaUUID / "comics") { id =>
          onSuccess(comicsDataSource.fetchBySeries(authInfo.user, id, limit, offset)) { results =>
            complete(Response(0, 0, 0, results))
          }
        }
      }
    }
}
