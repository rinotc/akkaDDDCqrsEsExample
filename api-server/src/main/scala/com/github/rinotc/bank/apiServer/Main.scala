package com.github.rinotc.bank.apiServer

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main extends App {
  val rootConfig = ConfigFactory.load()

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "bank-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val route: Route = {
    path("hello") {
      get {
        complete {
          HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>")
        }
      }
    }
  }

  val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

  println(s"Server now online. Please navigate to http://localhost:8080/hello \nPress RETURN to stop...")
  StdIn.readLine() // ユーザーがリターンを押すまで実行させる
  bindingFuture
    .flatMap(_.unbind()) // ポートからバインドを解除するトリガー
    .onComplete(_ => system.terminate()) // 完了したらシャットダウンする
}
