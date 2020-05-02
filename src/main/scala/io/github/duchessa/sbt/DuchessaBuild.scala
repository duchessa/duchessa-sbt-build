package io.github.duchessa.sbt

import sbt.Keys._
import sbt._

object DuchessaBuild extends AutoPlugin {

  sealed trait BuildProfile
  case object DevelopmentBuild extends BuildProfile
  case object DeploymentBuild extends BuildProfile


  override def buildSettings: Seq[Setting[_]] = Seq(
    buildProfile := {
      if (sys.props.get("profile").contains("deployment")) DeploymentBuild else DevelopmentBuild
    }
  )


  override def projectSettings: Seq[Setting[_]] = Seq(
    scalacOptions ++= scalaCompileSettings(buildProfile.value),
    name ~= formalize,
    pomIncludeRepository := (_ => false)
  )

  val buildProfile = settingKey[BuildProfile]("Defines the current BuildProfile. Defaults to DevelopmentBuild unless 'profile' property is detected and set to 'deployment'.")

  def scalaCompileSettings(profile: BuildProfile): Seq[String] = {
    def baseOptions = Seq(
      Opts.compile.deprecation,
      Opts.compile.unchecked,
      "-opt:unreachable-code",
      "-opt:simplify-jumps",
      "-opt:compact-locals",
      "-opt:copy-propagation",
      "-opt:redundant-casts",
      "-opt:box-unbox",
      "-opt:nullness-tracking",
      "-opt:closure-invocations",
      "-opt:allow-skip-core-module-init"
    )

    def developmentOptions = Seq(
      Opts.compile.explaintypes,
      "-Xcheckinit",
      "-Xlint:_",
      "-Ywarn-dead-code",
      "-Ywarn-extra-implicit",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused:implicits",
      "-Ywarn-unused:imports",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:params",
      "-Ywarn-unused:patvars",
      "-Ywarn-unused:privates",
      "-Ywarn-value-discard"
    )
    if (profile == DevelopmentBuild) baseOptions ++ developmentOptions else baseOptions
  }

  // Used to formalize project name for projects declared with the syntax 'val fooProject = project ...'
  private def formalize(name: String): String = name.split("-|(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")
    .map(_.capitalize).mkString(" ")
}
