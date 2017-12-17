package io.comiccloud.datasource

import java.util.UUID

import io.comiccloud.domain.{Series, User}

import scala.concurrent.Future

trait AbstractSeriesDataSource {

  def fetch(user: User, id: UUID): Future[Option[Series]]
  def fetchAll(user: User, limit: Int, offset: Int): Future[Seq[Series]]
  def create(user: User, series: Series): Future[Unit]

}
