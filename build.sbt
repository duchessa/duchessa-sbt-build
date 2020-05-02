name := "sbt-duchessa-build"

organization := "io.github.duchessa"

version := "0.4.0-SNAPSHOT"

startYear := Some(2020)

licenses := Seq("Apache 2.0 License" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

enablePlugins(SbtPlugin)

scalacOptions ++= Seq(
  Opts.compile.deprecation
)
