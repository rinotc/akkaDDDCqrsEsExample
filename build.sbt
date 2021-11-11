import Dependencies._

ThisBuild / organization := "com.github.rinotc"
ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / scalacOptions ++= Seq(
  "-feature",     // 明示的に import する必要のある機能を使用した場合、警告と場所を知らせる
  "-deprecation", // 非推奨のAPIの仕様している場合、警告と場所を知らせる
  //    "-Xfatal-warnings", // 警告が出た場合はコンパイル失敗させる
  "-Xlint",          // 推奨される警告の有効化
  "-Ywarn-dead-code" // デットコードがあれば警告する
)

lazy val `domain` = (project in file("domain"))
  .settings(
    name := "bank-domain",
    libraryDependencies ++= Seq()
  )

lazy val `api-server` = (project in file("api-server"))
  .settings(
    name := "bank-api-server",
    libraryDependencies ++= Seq(
      Logback.classic,
      TypeSafe.config,
      TypeSafe.Akka.actor,
      TypeSafe.Akka.http,
      TypeSafe.Akka.stream,
      TypeSafe.Akka.testKit
    )
  )

lazy val `interface` = (project in file("interface"))
  .settings(
    name := "bank-interface",
    libraryDependencies ++= Seq(
      TypeSafe.Akka.http
    )
  )

