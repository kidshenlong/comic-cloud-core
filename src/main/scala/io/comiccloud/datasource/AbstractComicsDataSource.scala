package io.comiccloud.datasource

import java.util.UUID

import io.comiccloud.domain.{Comic, User}

import scala.concurrent.Future

abstract class AbstractComicsDataSource {

  def comic(id: UUID, user: User): Future[Option[Comic]]
  def comics(user: User): Future[Seq[Comic]]
  def createComic(comic: Comic, user: User): Future[Unit]

}
