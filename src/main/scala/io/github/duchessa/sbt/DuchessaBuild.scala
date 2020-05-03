package io.github.duchessa.sbt

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin
import dotty.tools.sbtplugin.DottyPlugin.autoImport.isDotty

object DuchessaBuild extends AutoPlugin {

  sealed trait BuildProfile
  case object DevelopmentBuild extends BuildProfile
  case object DeploymentBuild extends BuildProfile

  override val requires = JvmPlugin

  override val trigger = allRequirements

  override def buildSettings: Seq[Setting[_]] = Seq(
    buildProfile := {
      if (sys.props.get("profile").contains("deployment")) DeploymentBuild else DevelopmentBuild
    }
  )


  override def projectSettings: Seq[Setting[_]] = {
    def fromRoot[A](key: SettingKey[A]) = key := (LocalRootProject / key).value
    def commonSettings = Seq(
      homepage,
      startYear,
      licenses,
      organization,
      organizationName,
      organizationHomepage,
      developers,
      apiURL,
      scmInfo,
      version,
      scalaVersion,
      crossScalaVersions)

    commonSettings.map(fromRoot(_)) ++ Seq(
      scalacOptions ++= ScalaCompileOptions(buildProfile.value, isDotty.value),
      name ~= formalize,
      pomIncludeRepository := (_ ⇒ false),
    )
  }

  val buildProfile = settingKey[BuildProfile]("Defines the current BuildProfile. Defaults to DevelopmentBuild unless 'profile' property is detected and set to 'deployment'.")

  // Used to formalize project name for projects declared with the syntax 'val fooProject = project ...'
  private def formalize(name: String): String = name.split("-|(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")
    .map(_.capitalize).mkString(" ")
}
