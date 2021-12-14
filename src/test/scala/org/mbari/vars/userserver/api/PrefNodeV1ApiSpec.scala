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

import org.mbari.vars.userserver.controllers.PrefNodeController
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.mbari.vars.userserver.model.PrefNode

/**
  * @author Brian Schlining
  * @since 2017-06-16T14:33:00
  */
class PrefNodeV1ApiSpec extends WebApiStack {

  private[this] val api = new PrefNodeV1Api(new PrefNodeController(daoFactory))
  private[this] val node = PrefNode("foo", "bar", "baz")

  addServlet(api, "/v1/prefs")

  "PrefNodeV1Api" should "POST" in {
    post("/v1/prefs",
      "name" -> node.name,
      "key" -> node.key,
      "value" -> node.value
    ) {
      status should be (200)
      val json = parse(body)
      val pref = json.extract[PrefNode]
      pref.name should be (node.name)
      pref.key should be (node.key)
      pref.value should be (node.value)
    }
  }

  it should "GET by name" in {
    get(s"/v1/prefs?name=${node.name}") {
      status should be (200)
      val json = parse(body)
      val prefs = json.extract[Array[PrefNode]]
      prefs.size should be (1)
      val pref = prefs.head
      pref.name should be (node.name)
      pref.key should be (node.key)
      pref.value should be (node.value)
    }
  }

  it should "GET by name and key" in {
    get(s"/v1/prefs?name=${node.name}&key=${node.key}") {
      status should be (200)
      val json = parse(body)
      val pref = json.extract[PrefNode]
      pref.name should be (node.name)
      pref.key should be (node.key)
      pref.value should be (node.value)
    }
  }

  // TODO: This test is no longer valid. startswith now requires a prefix query param
  // it should "GET by name that starts with" in {
  //   get(s"/v1/prefs/startswith${node.name.charAt(0)}") {
  //     status should be (200)
  //     val json = parse(body)
  //     val prefs = json.extract[Array[PrefNode]]
  //     prefs.size should be (1)
  //     val pref = prefs.head
  //     pref.name should be (node.name)
  //     pref.key should be (node.key)
  //     pref.value should be (node.value)
  //   }
  // }

  it should "GET by name that starts with using `prefix` query param" in {
    get(s"/v1/prefs/startswith?prefix=${node.name.charAt(0)}") {
      status should be (200)
      val json = parse(body)
      val prefs = json.extract[Array[PrefNode]]
      prefs.size should be (1)
      val pref = prefs.head
      pref.name should be (node.name)
      pref.key should be (node.key)
      pref.value should be (node.value)
    }
  }

  it should "PUT" in {
    put(s"/v1/prefs?name=${node.name}&key=${node.key}",
      "value" -> "crab") {
      status should be (200)
      val json = parse(body)
      val pref = json.extract[PrefNode]
      pref.name should be (node.name)
      pref.key should be (node.key)
      pref.value should be ("crab")
    }
  }

  it should "DELETE" in {
    delete(s"/v1/prefs?name=${node.name}&key=${node.key}") {
      status should be (200)
    }
  }




}
