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
    get(s"/v1/prefs/${node.name}") {
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
    get(s"/v1/prefs/${node.name}/${node.key}") {
      status should be (200)
      val json = parse(body)
      val pref = json.extract[PrefNode]
      pref.name should be (node.name)
      pref.key should be (node.key)
      pref.value should be (node.value)
    }
  }

  it should "GET by name that starts with" in {
    get(s"/v1/prefs/startswith/${node.name.charAt(0)}") {
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
    put(s"/v1/prefs/${node.name}/${node.key}",
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
    delete(s"/v1/prefs/${node.name}/${node.key}") {
      status should be (200)
    }
  }




}
