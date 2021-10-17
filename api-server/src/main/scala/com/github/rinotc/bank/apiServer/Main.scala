package com.github.rinotc.bank.apiServer

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import com.typesafe.config.ConfigFactory

object Main {

  def main(args: Array[String]): Unit = {

    val rootConfig = ConfigFactory.load()

    val rootBehavior = Behaviors.setup[Nothing] { context =>
      Behaviors.empty
    }
    val system = ActorSystem[Nothing](rootBehavior, "bank-system", rootConfig)
    println(system.printTree)
  }
}
