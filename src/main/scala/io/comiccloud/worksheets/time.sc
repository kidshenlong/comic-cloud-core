import java.text.DateFormat
import java.util.{Date, UUID}

import org.joda.time.DateTime

import scala.collection.mutable
import scala.util.Random
//import scalaoauth2.provider.AccessToken


//val time = new DateTime("Sun Dec 10 02:27:12 GMT 2017").toDate
//"2004-12-13T21:39:45.618-08:00"
val time = new DateTime("2017-12-10T01:27:12").toDate

case class AccessToken(token: String, refreshToken: Option[String], scope: Option[String], lifeSeconds: Option[Long], createdAt: Date, params: Map[String, String] = Map.empty[String, String]) {
  def isExpired: Boolean = expiresIn.exists(_ < 0)

  def expiresIn: Option[Long] = expirationTimeInMilis map { expTime =>
    (expTime - System.currentTimeMillis) / 1000
  }

  private def expirationTimeInMilis: Option[Long] = lifeSeconds map { lifeSecs =>
    createdAt.getTime + lifeSecs * 1000
  }
}

val token = AccessToken(Random.alphanumeric.take(10).mkString, None, None, Some(3600), time)

token.expiresIn

var user = "5e734947-dbc1-48fa-9d20-ebd4f1b78b3e"
var accessTokensStore: mutable.Map[UUID, mutable.Buffer[AccessToken]] = mutable.Map(
  UUID.fromString(user) -> mutable.Buffer(token)
)

accessTokensStore.get(UUID.fromString(user)).get.head.expiresIn
Thread.sleep(1000)
accessTokensStore.get(UUID.fromString(user)).get.head.expiresIn
Thread.sleep(1000)
accessTokensStore.get(UUID.fromString(user)).get.head.expiresIn
Thread.sleep(1000)
accessTokensStore.get(UUID.fromString(user)).get.head.expiresIn
Thread.sleep(1000)
accessTokensStore.get(UUID.fromString(user)).get.head.expiresIn
