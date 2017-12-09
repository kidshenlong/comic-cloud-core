package io.comiccloud

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import io.comiccloud.api.Api
import io.comiccloud.config.Config

import scala.concurrent.ExecutionContext

object Boot extends App with Api with Config{

  def startApplication() = {

    implicit val actorSystem: ActorSystem = ActorSystem()
    implicit val executor: ExecutionContext = actorSystem.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    Http().bindAndHandle(routes, httpInterface, httpPort)

  }

  startApplication()

}