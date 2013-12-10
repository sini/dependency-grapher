package me.forall.dependency_grapher

import xml.XML
import xml.factory.XMLLoader
import xml.Elem
import xml.Node
import java.net.URL
import java.io.File
import org.xml.sax.SAXParseException


trait ProjectParser[T] {
  def parseProject(t: T): Project
}

object ProjectParser {

  def convertToProject[A, B: ProjectParser](f: A => B, arg: A) = {
    try {
      val xml = f(arg)
      xml.toProject
    } catch {
      case e: SAXParseException =>
        println("Parse Error")
        Project(Artifact("","","")) //,Set.empty,Set.empty)
    }
  }

  class StringProjectParser extends ProjectParser[String] {
    def parseProject(str: String): Project =
      convertToProject(scala.xml.XML.loadString, str)
  }
  implicit val stringProjectParser = new StringProjectParser


  class URLProjectParser extends ProjectParser[URL] {
    def parseProject(url: URL): Project =
      convertToProject(scala.xml.XML.load(_: URL), url)
  }

  implicit val urlProjectParser = new URLProjectParser


  class FileProjectParser extends ProjectParser[File] {
    def parseProject(file: File): Project =
      convertToProject(scala.xml.XML.loadFile(_: File), file)
  }

  implicit val fileProjectParser = new FileProjectParser

  class ElemProjectParser extends ProjectParser[Elem] {
    def parseProject(t: Elem): Project = {
      Project(
      publishedArtifact = getArtifact(t)
      //dependencies = getDependencies(t),
      //repositories = getRepositories(t)
      )
    }
  }

  implicit val elemProjectParser = new ElemProjectParser

/*
  def elemToRepo(n: Node): Repository = {
    val name = (n \ "name").text
    val id = (n \ "id").text
    val url = (n \ "url").text
    val layout = (n \ "layout").text match {
      case "" => "default"
      case v => v
    }
    Repository(id = id, url = url, name = name, layout = layout)
  }
*/
  def getArtifact(n: Node) = {
    val artifactId = (n \ "artifactId").text
    val groupId = (n \ "groupId").text
    val version = (n \ "version").text
    val name = (n \ "name").text
    Artifact(artifactId = artifactId, groupId = groupId, version = version)
  }
/*
  def getRepositories(n: Elem) = {
    ((n \ "repositories" \ "repository") map elemToRepo).toSet
  }

  def getDependencies(n: Node) = {
    ((n \ "dependencies" \ "dependency") map (x => Dependency(getArtifact(x)))).toSet
  }
  */
}
