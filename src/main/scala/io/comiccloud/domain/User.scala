package io.comiccloud.domain

import java.util.UUID

case class User(id: UUID, username: String, hashedPassword: String)
