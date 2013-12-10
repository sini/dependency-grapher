package me.forall.dependency_grapher

import java.io.File

object DepencyGrapher extends App {

  logger.info("Starting up")

  val targetPomPath = args(0)

  println(targetPomPath)
  val repo = Repository("sonatype releases", "http://oss.sonatype.org/content/repositories/releases")
  val art = Artifact("junit", "junit", "4.11")
  val dep = Dependency(art)
  dep.isIn(repo)
  val targetPomFile = new File(targetPomPath)

  println(targetPomFile.toProject)

}
