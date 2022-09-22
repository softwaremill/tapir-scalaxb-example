val tapirVersion = "1.1.0"

lazy val rootProject = (project in file("."))
  .enablePlugins(ScalaxbPlugin)
  .settings(
    Seq(
      name := "tapir-scalaxb-example",
      version := "0.1.0-SNAPSHOT",
      organization := "com.softwaremill.tapir.scalaxb.example",
      scalaVersion := "2.13.8",
      libraryDependencies ++= Seq(
        "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
        "org.http4s" %% "http4s-blaze-server" % "0.23.12",
        "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
        "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.1.0", // needed to enrich schema used by swagger
        "ch.qos.logback" % "logback-classic" % "1.4.1",
        "org.dispatchhttp" %% "dispatch-core" % "1.2.0",
        "javax.xml.bind" % "jaxb-api" % "2.3.1",
        "org.scala-lang.modules" %% "scala-xml" % "1.3.0",
        "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
        "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
        "org.scalatest" %% "scalatest" % "3.2.13" % Test
      )
    )
  )
  .settings(
    Compile / scalaxb / scalaxbDispatchVersion := "1.2.0",
    Compile / scalaxb / scalaxbPackageName := "generated"
  )
