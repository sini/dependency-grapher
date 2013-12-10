package me.forall.dependency_grapher
import java.net.URL
import java.net.HttpURLConnection

case class Artifact(artifactId: String, groupId: String, version: String) 

case class Dependency(artifact: Artifact) { // exclusions: Set[Exclusion], scope: Scope){
  def isIn(repo: Repository): Boolean = {
    val path = repo.layout match {
      case "default" => 
        import repo._
        import artifact._
        new URL(s"$url/${groupId.replace(".", "/")}/$artifactId/$version/$artifactId-$version.pom")
      case _ => throw new Exception("Unhandled repository layout")
    }
    val conn = path.openConnection().asInstanceOf[HttpURLConnection]
    conn.setRequestMethod("HEAD")
    conn.connect()
    println("path: " + path + " response code: " + conn.getResponseCode())
    true
  }
}

case class Exclusion(groupId: String, artifactId: String) {
  def matches(artifact: Artifact): Boolean =
    (this.groupId, this.artifactId) == (artifact.groupId, artifact.artifactId)
}

sealed trait Packaging
case object War extends Packaging
case object Pom extends Packaging
case object Jar extends Packaging
case class OtherPackaging(name: String) extends Packaging

sealed trait Scope
case object Provided extends Scope
case object Test extends Scope
case object Compile extends Scope
case object Runtime extends Scope
case object System extends Scope

case class Repository(id: String, url: String, name: String = "", layout: String = "default")

case clasProject(
  publishedArtifact: Artifact,
//  packaging: Packaging,
//  parent: Artifact,
//  name: String,
//  description: String,
//  scm: URL,
  dependencies: Set[Dependency],
//  modules: Seq[Artifact],
  repositories: Set[Repository]
//  properties: Map[String, String]

//if packgiing is not defined, default jar
//groupid inherits from parent
//version inherits from parent
//every artifact has a unique (groupid,artifactid,version)
//module definition forces a parent relationship...
)

object ArtifactTree {
//  val root: Artifact = ???
}
