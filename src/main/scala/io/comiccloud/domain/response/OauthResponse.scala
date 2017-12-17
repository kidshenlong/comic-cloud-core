package io.comiccloud.domain.response

import java.util.Date

case class OauthResponse(
  access_token: String,
  refresh_token: String,
  expires: Long//todo Should this be exact time/date?
)
