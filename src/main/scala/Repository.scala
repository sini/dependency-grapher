package me.forall.dependency_grapher

import java.net.URL
import java.io.FileNotFoundException

trait Repository {

  def fetch(artifactId: String, groupId: String, version: String): Option[pom.Model]

  def fetch(dependency: pom.Dependency): Option[pom.Model] = for {
    a <- dependency.artifactId
    g <- dependency.groupId
    v <- dependency.version
    m <- fetch(a, g, v)
  } yield m

  def fetch(parent: pom.Parent): Option[pom.Model] = for {
    a <- parent.artifactId
    g <- parent.groupId
    v <- parent.version
    m <- fetch(a, g, v)
  } yield m

}

class LocalRepository extends Repository {
  def fetch(artifactId: String, groupId: String, version: String): Option[pom.Model] = {
    None
  }
}

class RemoteRepository(val base: pom.Repository) extends Repository {

  val id: String = base.id.getOrElse(throw new Exception("Repository must have an id!"))
  val layout: String = base.layout.getOrElse("default")
  val url: URL = base.url.map(new URL(_)).getOrElse(throw new Exception("Remote repository must have a url"))

  override def toString = s"Repsitory(id=$id, layout=$layout, url=$url)"

  def artifactUrl(artifactId: String, groupId: String): URL = layout match {
    case "default" =>
      new URL(s"$url/${groupId.replace(".", "/")}/$artifactId/maven-metadata.xml")
    case "legacy" =>
      ???
    case _ =>
      ???
  }

  def fetch(artifactId: String, groupId: String, version: String): Option[pom.Model] = try {
    val artifactMetadata = scalaxb.fromXML[meta.Metadata](xml.XML.load(artifactUrl(artifactId, groupId)))
    // hit groupid+artifactid/maven-metadata.xml
    // if version in metadata, 
    //   if version is snapshot, fetch maven-metadata under version folder
    //     fetch latest snapshot version... this is a weird case, easier to just ignore snapshots for right now re: local repos
    //   else fetch
    // else return false
    logger.info("Success!")
    None
  } catch {
    case _: FileNotFoundException => 
      logger.info("Failure.")
      None
    case e: Throwable =>
      logger.debug(e.getMessage())
      None
  }
}
