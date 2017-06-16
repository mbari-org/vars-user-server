package org.mbari.vars.userserver.api

import org.mbari.vars.userserver.{Constants, json4s}
import org.scalatest.BeforeAndAfterAll
import org.scalatra.swagger.{ApiInfo, Swagger}
import org.scalatra.test.scalatest.ScalatraFlatSpec

import scala.concurrent.ExecutionContext

/**
  * @author Brian Schlining
  * @since 2017-06-16T14:34:00
  */
class WebApiStack extends ScalatraFlatSpec with BeforeAndAfterAll {

  protected[this] val daoFactory = Constants.DAOFactory
  protected[this] implicit val executionContext = ExecutionContext.global
  protected[this] implicit val formats = json4s.CustomFormats

  protected[this] val apiInfo = ApiInfo(
    """vars-user-server""",
    """User and Preferences Manager - Server""",
    """http://localhost:8080/api-docs""",
    """brian@mbari.org""",
    """MIT""",
    """http://opensource.org/licenses/MIT"""
  )

  protected[this] implicit val swagger = new Swagger("1.2", "1.0.0", apiInfo)

}
