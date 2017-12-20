package io.comiccloud.datasource

import java.util.UUID

import io.comiccloud.domain.{Comic, User}

import scala.concurrent.Future

trait AbstractComicsDataSource {

  def fetch(user: User, id: UUID): Future[Option[Comic]]
  def fetchAll(user: User, limit: Int, offset: Int): Future[Seq[Comic]] //todo probably don't need this
  def create(user: User, comic: Comic): Future[Unit]
  def update(user: User, comic: Comic): Future[Unit]
  def delete(user: User, comic: Comic): Future[Unit]
  def fetchBySeries(user: User, seriesId: UUID, limit: Int, offset: Int): Future[Seq[Comic]]

}
