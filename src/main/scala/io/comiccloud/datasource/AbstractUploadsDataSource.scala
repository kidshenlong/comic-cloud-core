package io.comiccloud.datasource

import java.util.UUID

import io.comiccloud.domain.{Upload, User}

import scala.concurrent.Future

trait AbstractUploadsDataSource {

  def fetch(user: User, id: UUID): Future[Option[Upload]]
  def fetchAll(user: User, limit: Int, offset: Int): Future[Seq[Upload]]
  def create(user: User, upload: Upload): Future[Unit]

}
