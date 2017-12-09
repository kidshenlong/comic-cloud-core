package io.comiccloud.domain

import java.util.{Date, UUID}

case class Comic(id: UUID, issue: Double, contributors: Map[String, Seq[String]], release_date: Option[Date], series_id: UUID)
