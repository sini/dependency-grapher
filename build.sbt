import ScalaxbKeys._

scalaVersion := "2.10.3"

name := "dependency-grapher"

organization := "forall.me"

version := "0.0.1-SNAPSHOT"

libraryDependencies += "com.typesafe" %% "scalalogging-slf4j" % "1.0.1"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.13"

scalaxbSettings

packageName in scalaxb in Compile := "maven"

sourceGenerators in Compile <+= scalaxb in Compile
