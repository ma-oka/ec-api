name := """ec-api"""
organization := "com.github.ma-oka.ec-api"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

scalacOptions += "-Ypartial-unification"

routesImport ++= Seq(
  "be.venneborg.refined.play.RefinedPathBinders._",
  "be.venneborg.refined.play.RefinedQueryBinders._",
  "eu.timepit.refined.api.Refined",
  "eu.timepit.refined.numeric.Positive"
)

libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test,
  "org.typelevel" %% "cats-core" % "1.6.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.postgresql" % "postgresql" % "42.2.5",
  "com.typesafe.slick" %% "slick" % "3.3.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0",
  "com.github.tminglei" %% "slick-pg" % "0.17.2",
  "com.github.tminglei" %% "slick-pg_circe-json" % "0.17.2",
  "be.venneborg" %% "slick-refined" % "0.4.0",
  "be.venneborg" %% "play26-refined" % "0.3.0",
  "com.github.pureconfig" %% "pureconfig" % "0.10.2",
  "com.dripower" %% "play-circe" % "2711.0"
) ++ Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-refined"
).map(_ % "0.10.0") ++ Seq(
  "eu.timepit" %% "refined",
  "eu.timepit" %% "refined-cats",
  "eu.timepit" %% "refined-eval",
  "eu.timepit" %% "refined-jsonpath",
  "eu.timepit" %% "refined-pureconfig",
  "eu.timepit" %% "refined-scalacheck",
  "eu.timepit" %% "refined-scodec"
).map(_ % "0.9.4")

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
