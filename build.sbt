import sbt.Keys.libraryDependencies

name := "everbook_engine"

version := "0.1"

scalaVersion := "2.13.12"

val http4sVersion = "1.0.0-M40"
val googleAuthVersion = "1.19.0"
val pureConfigVersion = "0.17.4"
val circeVersion = "0.14.6"

libraryDependencies ++= Seq(

  // config
  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion,

  // http4s
  "org.http4s" %% "http4s-ember-client" % http4sVersion,
  "org.http4s" %% "http4s-ember-server" % http4sVersion,
  "org.http4s" %% "http4s-dsl"          % http4sVersion,
  "org.http4s" %% "http4s-circe"        % http4sVersion,

  // google sheets access
  "com.google.auth" % "google-auth-library-credentials" % googleAuthVersion,
  "com.google.auth" % "google-auth-library-oauth2-http" % googleAuthVersion,
  "com.google.apis" % "google-api-services-sheets" % "v4-rev612-1.25.0",
  "com.google.oauth-client" % "google-oauth-client-jetty" % "1.34.1",

  // google docs access
  "com.google.apis" % "google-api-services-docs" % "v1-rev61-1.25.0",

  "com.google.apis" % "google-api-services-drive" % "v3-rev197-1.25.0",

  // logging
  "ch.qos.logback" % "logback-classic" % "1.4.11",

  // json
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,

  // excel
  "com.norbitltd" %% "spoiwo" % "2.2.1"

)

scalacOptions += "-Ymacro-annotations"
