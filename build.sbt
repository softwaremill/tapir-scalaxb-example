val tapirVersion = "1.1.0"

lazy val rootProject = (project in file(".")).settings(
  Seq(
    name := "tapir-scalaxb-example",
    version := "0.1.0-SNAPSHOT",
    organization := "com.softwaremill.tapir.scalaxb.example",
    scalaVersion := "3.2.0",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
      "org.http4s" %% "http4s-blaze-server" % "0.23.12",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "ch.qos.logback" % "logback-classic" % "1.4.1",
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
      "org.scalatest" %% "scalatest" % "3.2.13" % Test
    )
  )
)
