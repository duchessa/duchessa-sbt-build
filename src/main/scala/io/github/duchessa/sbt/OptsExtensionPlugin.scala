package io.github.duchessa.sbt

import sbt.Keys._
import sbt._

import scala.language.implicitConversions

object OptsExtensionPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements
  override def requires: Plugins = DuchessaBuild

  object autoImport {
    implicit def optsToOptsSyntax(opts: Opts.type): OptsSyntax = OptsSyntax(opts)
    implicit def projectToProjectSyntax(project: Project): ProjectSyntax = ProjectSyntax(project)
  }

  object DuchessaOpts {
    val notPublished: Setting[Task[Boolean]] = publish / skip := true
    val apacheLicensed: Setting[Seq[(String, URL)]] = licenses += "Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")
  }

  final case class ProjectSyntax(underlying: Project) extends AnyVal {
    def notPublished: Project = underlying.settings(DuchessaOpts.notPublished)
    def apacheLicensed: Project = underlying.settings(DuchessaOpts.apacheLicensed)
  }

  final case class OptsSyntax(underlying: Opts.type) extends AnyVal {
    def duchessa: DuchessaOpts.type = DuchessaOpts
  }

}
