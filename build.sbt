name := "scala-practice"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "3.1.0" % "test"
libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "3.4.+",
  "com.h2database" % "h2" % "1.4.+",
  "org.slf4j" % "slf4j-api" % "1.7.1",
  "ch.qos.logback" % "logback-classic" % "1.2.+",
  "org.scalikejdbc" %% "scalikejdbc-test" % "3.4.+" % "test",
)
libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "4.6.0" % "test")
//libraryDependencies += "org.skinny-framework" %% "skinny-orm"      % "3.0.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"