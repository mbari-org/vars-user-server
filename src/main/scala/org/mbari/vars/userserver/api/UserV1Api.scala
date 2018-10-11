/*
 * Copyright 2017 Monterey Bay Aquarium Research Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mbari.vars.userserver.api

import org.mbari.vars.userserver.controllers.UserController
import org.scalatra.{ BadRequest, NotFound }

import scala.concurrent.ExecutionContext

class UserV1Api(controller: UserController)(implicit  val executor: ExecutionContext)
    extends ApiStack {

  before() {
    contentType = "application/json"
    response.headers += ("Access-Control-Allow-Origin" -> "*")
  }

  get("/") {
    controller.findAll
  }

  get("/:name") {
    val name = params.get("name").getOrElse(halt(BadRequest(
      body = """{"error": "A 'username' parameter is required"}""",
    )))
    controller.findByName(name).map({
      case None => halt(NotFound(body = s"""{"error": "No user with name '$name' was found"}"""))
      case Some(u) => u
    })
  }

  get("/role/:role") {
    val role = params.get("role").getOrElse(halt(BadRequest(
      body = """{"error": "A 'role' parameter is required"}""",
    )))
    controller.findAllByRole(role)
  }

  delete("/:name") {
    validateRequest()
    val name = params.get("name").getOrElse(halt(BadRequest(
      body = """{"error": "A 'username' parameter is required"}""",
    )))
    controller.delete(name)
  }

  post("/") {
    validateRequest()
    val username = params.get("username").getOrElse(halt(BadRequest(
      body = """{"error": "A 'username' parameter is required"}""",
    )))
    val password = params.get("password").getOrElse(halt(BadRequest(
      body = """{"error": "A 'password' parameter is required"}""",
    )))
    val role = params.get("role").getOrElse(halt(BadRequest(
      body = """{"error": "A 'role' parameter is required"}""",
    )))
    val firstName = params.get("firstName")
    val lastName = params.get("lastName")
    val affiliation = params.get("affiliation")
    val email = params.get("email")
    controller.create(username, password, role, firstName, lastName, affiliation, email)
  }

  put("/:name") {
    validateRequest()
    val username = params.get("name").getOrElse(halt(BadRequest(
      body = """{"error": "A 'username' parameter is required"}""",
    )))
    val role = params.get("role")
    val firstName = params.get("firstName")
    val lastName = params.get("lastName")
    val affiliation = params.get("affiliation")
    val email = params.get("email")
    controller.update(username, role, firstName, lastName, affiliation, email)
      .map({
        case None => halt(NotFound(body = s"""{"error": "Failed to update user named '$username'"}"""))
        case Some(u) => u
      })
  }

}