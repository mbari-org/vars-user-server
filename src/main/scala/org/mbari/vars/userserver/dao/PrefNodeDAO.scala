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

package org.mbari.vars.userserver.dao

import org.mbari.vars.userserver.model.PrefNode

/**
 * @author Brian Schlining
 * @since 2017-06-05T10:45:00
 */
trait PrefNodeDAO extends DAO {

  def create(prefNode: PrefNode): Unit

  def update(prefNode: PrefNode): Option[PrefNode]

  def delete(prefNode: PrefNode): Unit

  def findByNodeNameAndKey(nodeName: String, key: String): Option[PrefNode]

  /**
   * Return names of children below the node with this name
   * @param name The name of the node
   * @return names of children below the node
   */
  def findByNodeName(name: String): Iterable[PrefNode]

  def findByNodeNameLike(name: String): Iterable[PrefNode]

}
