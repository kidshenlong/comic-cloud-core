import java.util.Date

import org.json4s.{DefaultFormats, Formats}
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write, writePretty}
import org.json4s.jackson.JsonMethods._

import scala.util.Random
import scalaoauth2.provider.AccessToken

//formats: Formats = DefaultFormats

implicit val formats: Serialization.type = Serialization
implicit val jsonFormats: Formats = DefaultFormats

val token = AccessToken(Random.alphanumeric.take(10).mkString, None, None, Some(3600), new Date)


val ser = writePretty(token)