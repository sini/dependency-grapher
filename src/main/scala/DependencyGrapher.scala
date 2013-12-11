package me.forall.dependency_grapher

import java.io.File
import pom._

object DepencyGrapher extends App {

  logger.info("Starting up")

  val targetPomPath = args(0)
  val file = new File(targetPomPath)
  val pom = scala.xml.XML.loadFile(file)
  val project = scalaxb.fromXML[Model](pom)
  val deps: Dependencies = project.dependencies.get
  println(deps)
  for(x <- deps.dependency) {
    println(x)
  }
  //val x = maven.ActivationOS()
  //  val repo = Repository("sonatype releases", "http://oss.sonatype.org/content/repositories/releases")
//  val art = Artifact("junit", "junit", "4.11")
//  val dep = Dependency(art)
//  dep.isIn(repo)
//  val targetPomFile = new File(targetPomPath)

//  println(targetPomFile.toProject)

}
