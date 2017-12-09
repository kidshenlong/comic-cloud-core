package io.comiccloud.datasource

import java.util.UUID

import io.comiccloud.domain.{Comic, User}

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

class InMemoryComicsDataSource()/*(implicit executionContext: ExecutionContext)*/ extends AbstractComicsDataSource {

  private val comicStore: mutable.Buffer[Comic] = mutable.Buffer(
    Comic(UUID.fromString("3371ada7-ba97-49bf-97f0-fab4c9982cea"), 1, Map("writer" -> Seq("Brian K. Vaughn"), "art" -> Seq("Fiona Staples")), None, UUID.randomUUID())
  )

  override def comic(id: UUID, user: User): Future[Option[Comic]] = {
    Future.successful(comicStore.find(_.id == id))
  }

  override def comics(user: User): Future[Seq[Comic]] = {
    Future.successful(comicStore)
  }

  override def createComic(comic: Comic, user: User): Future[Unit] = {
    Future.successful{
      comicStore += comic
      Unit
    }
  }
}
