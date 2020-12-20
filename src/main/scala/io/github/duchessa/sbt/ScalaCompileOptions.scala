package io.github.duchessa.sbt

import io.github.duchessa.sbt.DuchessaBuild.{BuildProfile, DeploymentBuild, DevelopmentBuild}

object ScalaCompileOptions {

  def apply(profile: BuildProfile, dotty: Boolean): Seq[String] = (profile, dotty) match {
    case (DevelopmentBuild, true) ⇒ dottycBaseOptions ++ dottycDevelopmentOptions
    case (DeploymentBuild, true) ⇒ dottycBaseOptions
    case (DevelopmentBuild, false) ⇒ scalacBaseOptions ++ scalacDevelopmentOptions
    case (DeploymentBuild, false) ⇒ scalacBaseOptions
  }

  def dottycBaseOptions = Seq(
    "-deprecation",
    "-explain-types",
    "-explain",
    "-unchecked"
  )

  def dottycDevelopmentOptions = Seq(
    "-Xfatal-warnings",
    "-feature"
  )

  def scalacBaseOptions = Seq(
    "-deprecation",
    "-unchecked",
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

  def scalacDevelopmentOptions = Seq(
    "-explaintypes",
    "-feature",
    "-Xcheckinit",
    "-Xdev",
    "-Xlint:_",
    "-Werror",
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

}
