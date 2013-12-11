import sbt._

object Build extends sbt.Build {
  import sbtscalaxb.Plugin._
  import ScalaxbKeys._
  import Keys._

  val Pom = config("pom") extend(Compile)
  val Meta = config("metadata") extend(Compile)
  lazy val app = Project("app", file("."), settings = Defaults.defaultSettings ++ codeGenSettings)

  def customScalaxbSettings(base: String): Seq[Project.Setting[_]] = Seq(
    sources <<= xsdSource map { xsd => Seq(xsd / (base + ".xsd")) },
    packageName := "me.forall.dependency_grapher." + base,
    protocolFileName := base + "_xmlprotocol.scala"
  )

  def codeGenSettings: Seq[Project.Setting[_]] =
    inConfig(Pom)(baseScalaxbSettings ++ inTask(scalaxb)(customScalaxbSettings("pom"))) ++
    inConfig(Meta)(baseScalaxbSettings ++ inTask(scalaxb)(customScalaxbSettings("meta"))) ++ Seq(
      sourceGenerators in Compile <+= scalaxb in Pom,
      sourceGenerators in Compile <+= scalaxb in Meta
    )
}
