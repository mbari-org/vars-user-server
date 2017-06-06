package org.mbari.vars.userserver.api

import org.mbari.vars.userserver.controllers.UserController
import org.scalatra.BadRequest
import org.scalatra.swagger.Swagger

import scala.concurrent.ExecutionContext

class UserV1Api(controller: UserController)
               (implicit val swagger: Swagger, val executor: ExecutionContext)
    extends ApiStack {

  override protected def applicationDescription: String = "User V1 API"
  override protected val applicationName: Option[String] = Some.apply(getClass.getSimpleName)

  before() {
    contentType = "application/json"
    response.headers += ("Access-Control-Allow-Origin" -> "*")
  }

  get("/") {
    controller.findAll
  }

  get("/:name") {
    val name = params.get("name").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'username' path parameter is required"
    )))
    controller.findByName(name)
  }

  get("/role/:role") {
    val role = params.get("role").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'role' path parameter is required"
    )))
    controller.findAllByRole(role)
  }

  delete("/:name") {
    validateRequest()
    val name = params.get("name").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'username' path parameter is required"
    )))
    controller.delete(name)
  }

  post("/") {
    validateRequest()
    val username = params.get("username").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'username' parameter is required"
    )))
    val password = params.get("password").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'password' parameter is required"
    )))
    val role = params.get("role").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'role' parameter is required"
    )))
    val firstName = params.get("first_name")
    val lastName = params.get("last_name")
    val affiliation = params.get("affiliation")
    val email = params.get("email")
    controller.create(username, password, role, firstName, lastName, affiliation, email)
  }

  put("/") {
    validateRequest()
    val username = params.get("username").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'username' parameter is required"
    )))
    val password = params.get("password").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'password' parameter is required"
    )))
    val role = params.get("role").getOrElse(halt(BadRequest(
      body = "{}",
      reason = "A 'role' parameter is required"
    )))
    val firstName = params.get("first_name")
    val lastName = params.get("last_name")
    val affiliation = params.get("affiliation")
    val email = params.get("email")
    controller.update(username, password, role, firstName, lastName, affiliation, email)
  }

}