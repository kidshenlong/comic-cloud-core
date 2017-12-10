package io.comiccloud.domain.request

case class OauthRequest(client_id: String, client_secret: String, grant_type: String, username: String, password: String)
