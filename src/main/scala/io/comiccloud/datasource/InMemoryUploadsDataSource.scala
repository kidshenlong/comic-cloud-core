package io.comiccloud.datasource

import java.util.{Date, UUID}

import io.comiccloud.domain.{Series, Upload, User}

import scala.collection.mutable
import scala.concurrent.Future

class InMemoryUploadsDataSource()/*(implicit executionContext: ExecutionContext)*/ extends AbstractUploadsDataSource {

  private val uploadsStore: mutable.Buffer[Upload] = mutable.Buffer(
    Upload(
      UUID.fromString("7e6ce6c8-db92-442a-9fff-1f8a8f3eece8"),
      0
    )
  )

  override def fetch(user: User, id: UUID): Future[Option[Upload]] = {
    Future.successful(uploadsStore.find(_.id == id))
  }

  override def fetchAll(user: User, limit: Int, offset: Int): Future[Seq[Upload]] = {
    Future.successful(uploadsStore)
  }

  override def create(user: User, upload: Upload): Future[Unit] = {
    Future.successful{
      uploadsStore += upload
      Unit
    }
  }
}
