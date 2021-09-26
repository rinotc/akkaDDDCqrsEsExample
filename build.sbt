lazy val baseSettings = Seq(
  organization := "com.github.rinotc",
  version := "0.1",
  scalaVersion := "2.13.6",
  scalacOptions ++= Seq(
    "-feature",         // 明示的に import する必要のある機能を使用した場合、警告と場所を知らせる
    "-deprecation",     // 非推奨のAPIの仕様している場合、警告と場所を知らせる
    "-Xfatal-warnings", // 警告が出た場合はコンパイル失敗させる
    "-Xlint",           // 推奨される警告の有効化
    "-Ywarn-dead-code"  // デットコードがあれば警告する
  )
)

lazy val `api-server` = (project in file("api-server"))
  .settings(baseSettings)
  .settings(
    name := "bank-api-server"
  )
