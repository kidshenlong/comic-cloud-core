package io.comiccloud.domain

import java.util.UUID

case class User(id: UUID, name: String, hashedPassword: String)
