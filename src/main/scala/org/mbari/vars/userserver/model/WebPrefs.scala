package org.mbari.vars.userserver.model

import java.util.prefs.{AbstractPreferences, Preferences}

import org.mbari.vars.userserver.dao.PrefNodeDAO

/**
  * @author Brian Schlining
  * @since 2017-06-05T11:22:00
  */
class WebPrefs(val dao: PrefNodeDAO, parentPrefs: AbstractPreferences = null, name: String = "")
  extends AbstractPreferences(parentPrefs, name) {

  override def flushSpi(): Unit = { /* do nothing */ }

  override def childSpi(name: String): AbstractPreferences = new WebPrefs(dao, this, name)

  override def syncSpi(): Unit = { /* do nothing */ }

  override def putSpi(key: String, value: String): Unit = {
    dao.findByNodeNameAndKey(absolutePath(), key) match {
      case None =>
        dao.insert(PrefNode(absolutePath(), key, value))
      case Some(node) =>
        if (node.value != value) dao.update(node.copy(value = value))
    }
  }

  override def childrenNamesSpi(): Array[String] = {
    val parentNodeName = absolutePath()
    val nodes = dao.findByNodeNameLike(parentNodeName)
    val childNames = for (node <- nodes) yield {
      val nodeName = node.name
      // Strip off base path
      var childPath = nodeName.substring(parentNodeName.length, nodeName.length)
      // If path starts with '/' remove it
      if (childPath.startsWith("/")) childPath = childPath.substring(1, childPath.length)
      // Take up to the first '/' (or all if no slash is present)
      if (childPath.indexOf("/") >= 0) childPath.substring(0, childPath.indexOf("/"))
          else childPath
    }
    childNames.toArray.distinct
  }

  override def removeSpi(key: String): Unit = dao.findByNodeNameAndKey(absolutePath(), key) match {
    case None => // do nothing
    case Some(node) => dao.delete(node)
  }

  override def keysSpi(): Array[String] = dao.findByNodeName(absolutePath())
    .map(_.key)
    .toArray

  override def removeNodeSpi(): Unit = dao.findByNodeNameLike(absolutePath())
    .foreach(dao.delete)

  override def getSpi(key: String): String = dao.findByNodeNameAndKey(absolutePath(), key) match {
    case None => null
    case Some(node) => node.value
  }
}
