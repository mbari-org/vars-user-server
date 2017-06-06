package org.mbari.vars.userserver.api

import org.scalatra.BadRequest
import org.scalatra.swagger.Swagger

import scala.concurrent.ExecutionContext

class HelloApi(implicit val swagger: Swagger, val executor: ExecutionContext)
    extends ApiStack {

  override protected def applicationDescription: String = "Hello API"
  override protected val applicationName: Option[String] = Some.apply(getClass.getSimpleName)

  before() {
    contentType = "application/json"
    response.headers += ("Access-Control-Allow-Origin" -> "*")
  }

  get("/") {
    """{"response": "Hello World"}"""
  }

  get("/name/:name") {
    val name = params.get("name").getOrElse(halt(BadRequest(
          body = "{}",
          reason = "A 'name' parameter is required"
        )))
    s"""{"response": "Hello $name"}"""
  }
}