name := "scala-practice"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "3.1.0" % "test"
libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "3.4.+",
  "com.h2database" % "h2" % "1.4.+",
  "ch.qos.logback" % "logback-classic" % "1.2.+"
)
