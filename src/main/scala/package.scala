package me.forall
import org.slf4j.LoggerFactory
import ch.qos.logback.core.util.StatusPrinter
import ch.qos.logback.classic.LoggerContext

package object dependency_grapher {

  lazy val logger = {
    StatusPrinter.print((LoggerFactory.getILoggerFactory).asInstanceOf[LoggerContext])
    LoggerFactory.getLogger("DependecyGrapher")
  }

/*  implicit class ExtendWithToProject[T: ProjectParser](t: T) {
    def toProject: Project = implicitly[ProjectParser[T]].parseProject(t)
  }
*/
}
