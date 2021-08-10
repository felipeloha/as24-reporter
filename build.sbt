name := """as23-reporter"""
organization := "com.as23"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.8"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.as23.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.as23.binders._"
