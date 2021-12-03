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

import org.json4s.jackson.JsonMethods.parse
import org.mbari.vars.userserver.controllers.UserController
import org.mbari.vars.userserver.model.{PrefNode, User}

/**
  * @author Brian Schlining
  * @since 2017-06-16T14:33:00
  */
class UserV1ApiSpec extends WebApiStack {

  private[this] val api = new UserV1Api(new UserController(daoFactory))
  private[this] val user = User("foo", "bar", "Admin", Some("MBARI"), Some("Brian"),
    Some("Schlining"), Some("brian@mbari.org"), isEncrypted = false)

  addServlet(api, "/v1/users")

  "UserV1Api" should "POST" in {
    post("/v1/users",
      "username" -> user.username,
      "password" -> user.password,
      "role" -> user.role,
      "lastName" -> user.lastName.get,
      "firstName" -> user.firstName.get,
      "affiliation" -> user.affiliation.get,
      "email" -> user.email.get
    ) {
      status should be (200)
      header("Content-Type") should startWith ("application/json")
      val json = parse(body)
      val u = json.extract[User]
      userCheck(user, u)
    }
  }

  it should "GET by name" in {
    get(s"/v1/users/${user.username}") {
      status should be (200)
      val json = parse(body)
      val u = json.extract[User]
      userCheck(user, u)
    }
  }

  it should "GET by role" in {
    get(s"/v1/users/role/${user.role}") {
      status should be (200)
      val json = parse(body)
      val u = json.extract[Array[User]]
      u.foreach(println)
      u.size should be (1)
      userCheck(user, u.head)
    }
  }

  it should "GET all"  in {
    get("/v1/users") {
      status should be (200)
      val json = parse(body)
      val u = json.extract[Array[User]]
      u.foreach(println)
      u.size should be (1)
      userCheck(user, u.head)
    }
  }

  it should "PUT" in {
    put(s"/v1/users/${user.username}",
      "firstName" -> "Bob",
      "lastName" -> "Ben",
      "password" -> "2020") {
      status should be(200)
      val json = parse(body)
      val u = json.extract[User]
      u.username should be (user.username)
      u.firstName.get should be ("Bob")
      u.lastName.get should be ("Ben")
    }
  }

  it should "DELETE" in {
    delete(s"/v1/users/${user.username}") {
      status should be (200)
    }
  }

  def userCheck(u0: User, u1: User): Unit = {
    u0.username should be (u1.username)
    u0.password should not be (u1.password)
    u0.role should be (u1.role)
    u0.affiliation should be (u1.affiliation)
    u0.firstName should be (u1.firstName)
    u0.lastName should be (u1.lastName)
    u0.email should be (u0.email)
    u0.isEncrypted should not be u1.isEncrypted
  }

}
