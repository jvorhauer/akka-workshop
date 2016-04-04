import sbt.Keys._

val akkaVersion = "2.4.2"

lazy val `akkaws` = (project in file(".")).settings(
  name := "akkaws-csv",
  organization := "com.jdriven",
  version := "1.0.0-SNAPSHOT",
  scalaVersion := "2.11.8",

  ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },

  libraryDependencies ++= Seq(
    "com.typesafe.akka" %%  "akka-actor"   % akkaVersion,
    "com.typesafe.akka" %%  "akka-slf4j"   % akkaVersion,
    "com.typesafe.akka" %%  "akka-testkit" % akkaVersion  % "test",
    "org.scalatest"     %%  "scalatest"    % "2.2.6"      % "test"
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:implicitConversions",
    "-language:postfixOps",    // duration postfixes
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-target:jvm-1.8"
  ),

  fork in run := true,
  cancelable in Global := true,

  sources in (Compile, doc) := Seq.empty,
  publishArtifact in (Compile, packageDoc) := false

)
