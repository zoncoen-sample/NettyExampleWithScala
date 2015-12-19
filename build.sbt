lazy val root = (project in file(".")).settings(
  name := "NettyExampleWithScala",
  version := "1.0",
  scalaVersion := "2.11.7",
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % "2.11.7",
    "io.netty" % "netty-all" % "4.0.4.Final"
  )
)