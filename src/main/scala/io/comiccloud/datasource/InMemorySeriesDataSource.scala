package io.comiccloud.datasource

import java.util.{Date, UUID}

import io.comiccloud.domain.{Comic, Series, User}

import scala.collection.mutable
import scala.concurrent.Future

class InMemorySeriesDataSource()/*(implicit executionContext: ExecutionContext)*/ extends AbstractSeriesDataSource {

  private val seriesStore: mutable.Buffer[Series] = mutable.Buffer(
    Series(
      UUID.fromString("34b8dbb3-1015-4ee8-9b23-c0a9b3580d9f"),
      "Saga",
      new Date(2012, 3, 1), //todo don't use Java date
      "Image",
      ""
    )
  )

  override def fetch(user: User, id: UUID): Future[Option[Series]] = {
    Future.successful(seriesStore.find(_.id == id))
  }

  override def fetchAll(user: User, limit: Int, offset: Int): Future[Seq[Series]] = {
    Future.successful(seriesStore)
  }

  override def create(user: User, series: Series): Future[Unit] = {
    Future.successful{
      seriesStore += series
      Unit
    }
  }

  override def update(user: User, series: Series): Future[Unit] = ???

  override def delete(user: User, series: Series): Future[Unit] = ???
}
