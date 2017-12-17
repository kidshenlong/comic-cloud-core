package io.comiccloud.domain

import java.util.{Date, UUID}

case class Series(
  id: UUID,
  title: String,
  start_date: Date,
  publisher: String,
  cover_image: String //todo this might live somewhere else
)
