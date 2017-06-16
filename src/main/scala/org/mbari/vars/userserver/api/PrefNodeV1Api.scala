package org.mbari.vars.userserver.api

import org.mbari.vars.userserver.controllers.PrefNodeController
import org.scalatra.{BadRequest, NotFound}
import org.scalatra.swagger.Swagger

import scala.concurrent.ExecutionContext
import org.scalatra.json._

/**
  * @author Brian Schlining
  * @since 2017-06-05T14:22:00
  */
class PrefNodeV1Api(controller: PrefNodeController)(implicit val swagger: Swagger, val executor: ExecutionContext)
  extends ApiStack {

  override protected def applicationDescription: String = "PrefNode V1 API"
  override protected val applicationName: Option[String] = Some.apply(getClass.getSimpleName)

  before() {
    contentType = "application/json"
    response.headers += ("Access-Control-Allow-Origin" -> "*")
  }

  get("/:name") {
    val name = params.get("name").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'name' parameter is required"
    )))
    controller.findByNodeName(name)
  }

  get("/:name/:key") {
    val name = params.get("name").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'name' parameter is required"
    )))
    val key = params.get("key").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'key' parameter is required"
    )))
    controller.findByNodeNameAndKey(name, key)
      .map({
        case None => halt(NotFound(body = "{}", reason = s"Did nor find pref with name: '$name and key '$key'"))
        case Some(p) => p
      })
  }

  get("/startswith/:name") {
    val name = params.get("name").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'name' parameter is required"
    )))
    controller.findByNodeNameLike(name)
  }

  post("/") {
    validateRequest()
    val name = params.get("name").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'name' parameter is required"
    )))
    val key = params.get("key").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'key' parameter is required"
    )))
    val value = params.get("value").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'value' parameter is required"
    )))
    controller.create(name, key, value)
  }

  put("/:name/:key") {
    validateRequest()
    val name = params.get("name").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'name' parameter is required"
    )))
    val key = params.get("key").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'key' parameter is required"
    )))
    val value = params.get("value").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'value' parameter is required"
    )))
    controller.update(name, key, value)
  }

  delete("/:name/:key") {
    validateRequest()
    val name = params.get("name").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'name' parameter is required"
    )))
    val key = params.get("key").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'key' parameter is required"
    )))
    controller.delete(name, key)
  }



}