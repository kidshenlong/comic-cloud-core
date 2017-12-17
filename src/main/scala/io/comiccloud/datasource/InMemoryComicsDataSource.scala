package io.comiccloud.datasource

import java.util.UUID

import io.comiccloud.domain.{Comic, User}

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

class InMemoryComicsDataSource()/*(implicit executionContext: ExecutionContext)*/ extends AbstractComicsDataSource {

  private val comicStore: mutable.Buffer[Comic] = mutable.Buffer(
    Comic(
      id = UUID.fromString("3371ada7-ba97-49bf-97f0-fab4c9982cea"),
      issue = 1,
      contributors = Map("writer" -> Seq("Brian K. Vaughn"), "art" -> Seq("Fiona Staples")),
      release_date = None,
      series_id = UUID.fromString("34b8dbb3-1015-4ee8-9b23-c0a9b3580d9f"),
      images = Map()
    )
  )

  override def fetch(user: User, id: UUID): Future[Option[Comic]] = {
    Future.successful(comicStore.find(_.id == id))
  }

  override def fetchAll(user: User, limit: Int, offset: Int): Future[Seq[Comic]] = {
    Future.successful(comicStore)
  }

  override def create(user: User, comic: Comic): Future[Unit] = {
    Future.successful{
      comicStore += comic
      Unit
    }
  }

  override def fetchBySeries(user: User, seriesId: UUID, limit: Int, offset: Int): Future[Seq[Comic]] = {
    Future.successful(comicStore.filter(_.series_id == seriesId))
  }
}
