package org.mbari.vars.userserver.model

import vars.jpa.PreferenceNode

/**
  * @author Brian Schlining
  * @since 2017-06-05T10:41:00
  */
case class PrefNode(name: String, key: String, value: String) {

  def toPreferenceNode : PreferenceNode = {
    val n = new PreferenceNode()
    n.setNodeName(name)
    n.setPrefKey(key)
    n.setPrefValue(value)
    n
  }
}

object PrefNode {

  def apply(preferenceNode: PreferenceNode) : PrefNode =
    PrefNode(preferenceNode.getNodeName,
      preferenceNode.getPrefKey,
      preferenceNode.getPrefValue)
}
