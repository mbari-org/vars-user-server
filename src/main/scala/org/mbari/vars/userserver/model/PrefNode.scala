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

package org.mbari.vars.userserver.model

import vars.jpa.PreferenceNode

/**
 * @author Brian Schlining
 * @since 2017-06-05T10:41:00
 */
case class PrefNode(name: String, key: String, value: String) {

  def toPreferenceNode: PreferenceNode = {
    val n = new PreferenceNode()
    n.setNodeName(name)
    n.setPrefKey(key)
    n.setPrefValue(value)
    n
  }
}

object PrefNode {

  def apply(preferenceNode: PreferenceNode): PrefNode =
    PrefNode(
      preferenceNode.getNodeName,
      preferenceNode.getPrefKey,
      preferenceNode.getPrefValue
    )
}
