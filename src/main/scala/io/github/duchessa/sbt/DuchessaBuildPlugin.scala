package io.github.duchessa.sbt

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

object DuchessaBuildPlugin extends AutoPlugin {

  sealed trait BuildProfile
  case object DevelopmentBuild extends BuildProfile
  case object DeploymentBuild extends BuildProfile

  override val requires = JvmPlugin

  override val trigger = allRequirements

  override def buildSettings: Seq[Setting[_]] = Seq(
    versionScheme := Some("semver-spec"),
    autoImport.buildProfile := {
      if (sys.props.get("profile").exists(p => p.startsWith("deployment") || p.startsWith("production")))
        DeploymentBuild else DevelopmentBuild
    },
    autoImport.isDotty := scalaVersion.value.startsWith("0.") || scalaVersion.value.startsWith("3.")
  )


  override def projectSettings: Seq[Setting[_]] = {
    def fromRoot[A](key: SettingKey[A]): Setting[A] = key := (LocalRootProject / key).value
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
      scalacOptions ++= ScalaCompileOptions(autoImport.buildProfile.value, autoImport.isDotty.value),
      name ~= formalize,
      pomIncludeRepository := (_ ⇒ false),
    )
  }

  object autoImport {
    val buildProfile = settingKey[BuildProfile]("Defines the current BuildProfile. Defaults to DevelopmentBuild unless 'profile' property is detected and set to 'deployment'.")
    val isDotty = settingKey[Boolean]("Defaults to 'true' when building against Scala 3.x")
  }

  // Used to formalize project name for projects declared with the syntax 'val fooProject = project ...'
  private def formalize(name: String): String = name.split("-|(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")
    .map(_.capitalize).mkString(" ")
}
