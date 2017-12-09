package io.comiccloud.domain.response

case class Response[T](
  total: Int,
  limit: Int,
  offset: Int,
  results: Seq[T]
)
