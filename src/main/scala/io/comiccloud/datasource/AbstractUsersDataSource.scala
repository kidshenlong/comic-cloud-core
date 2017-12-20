package io.comiccloud.datasource

import java.util.UUID

import io.comiccloud.domain.{Upload, User}

import scala.concurrent.Future

trait AbstractUsersDataSource {

  def fetch(user: User, id: UUID): Future[Option[User]]
  def create(user: User): Future[Unit]
  def update(user: User): Future[Unit]

}
