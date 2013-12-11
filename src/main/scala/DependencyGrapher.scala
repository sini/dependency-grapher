package me.forall.dependency_grapher

import java.io.File

object DepencyGrapher extends App {

  logger.info("Starting up")

  val targetPomPath = args(0)
  val file = new File(targetPomPath)
  val foo = scala.xml.XML.loadFile(file)
  val project = scalaxb.fromXML[pom.Model](foo)

  def transform[In, Mid, Final](in: In)(firstOp: In => Option[Mid])(flatOp: Mid => Seq[Final]): Seq[Final] =
    firstOp(in).toSeq.flatMap(flatOp)

  val reps: Seq[pom.Repository]  =
    transform(project)(_.repositories)(_.repository)

  val deps: Seq[pom.Dependency] =
    transform(project)(_.dependencies)(_.dependency)

  val exclusions: Map[pom.Dependency, Seq[pom.Exclusion]] = deps.map { dep =>
    (dep, transform(dep)(_.exclusions)(_.exclusion))
  }.toMap

  println(exclusions)

  for {
       pomRepo <- reps
       remote = new RemoteRepository(pomRepo)
       pomDep <- deps
    }  {
      // logger.info("repo: " + remote + " dep: " + pomDep)
      // logger.info(remote.fetch(pomDep).toString)
    }
  // val remote = new RemoteRepository(
  //     pom.Repository(
  //       url = Some("http://repo1.maven.org/maven2"),
  //       id = Some("central")
  //     )
  // )
  // println(remote.fetch("commons-configuration", "commons-configuration", "1.1.0"))
}
