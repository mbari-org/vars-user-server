import javax.servlet.ServletContext

import org.mbari.vars.userserver.api.HelloApi
import org.scalatra.LifeCycle
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext

/**
 *
 *
 * @author Brian Schlining
 * @since 2016-05-20T14:41:00
 */
class ScalatraBootstrap extends LifeCycle {

  private[this] val log = LoggerFactory.getLogger(getClass)

  override def init(context: ServletContext): Unit = {

    println("STARTING UP NOW")

    implicit val executionContext = ExecutionContext.global

    val helloApi = new HelloApi

    context.mount(helloApi, "/v1/hello")

  }

}
