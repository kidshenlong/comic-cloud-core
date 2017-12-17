package io.comiccloud.datasource

import java.util.UUID

import io.comiccloud.domain.User

import scala.collection.mutable
import scalaoauth2.provider.AccessToken

object OauthMockData {

  val clientCredentialsStore: Map[String, String] = Map("client_id_1" -> "client_secret_1")
  //private val clientCredentialsStore2: Seq[clientCredential] = Seq(clientCredential("client_id_1", "client_secret_1"))

  val userStore: Seq[User] = Seq(
    User(UUID.randomUUID(), "kidshenlong", "password"),
    User(UUID.randomUUID(), "other_user", "password")
  )

  case class AccessTokenInformation(accessToken: AccessToken, clientId: String)

  var accessTokensStore: mutable.ListMap[UUID, mutable.Buffer[AccessTokenInformation]] = mutable.ListMap()

}
