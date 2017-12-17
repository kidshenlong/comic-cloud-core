package io.comiccloud.domain

import java.util.UUID

case class Upload(
  id: UUID,
  processing_progress: Double
)
