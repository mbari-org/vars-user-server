package org.mbari.vars.userserver.dao

import org.mbari.vars.userserver.model.PrefNode

/**
  * @author Brian Schlining
  * @since 2017-06-05T10:45:00
  */
trait PrefNodeDAO extends DAO {

  def insert(prefNode: PrefNode)

  def update(prefNode: PrefNode)

  def delete(prefNode: PrefNode)

  def findByNodeNameAndKey(nodeName: String, key: String): Option[PrefNode]

  /**
    * Return names of children below the node with this name
    * @param name The name of the node
    * @return names of children below the node
    */
  def findByNodeName(name: String): Iterable[PrefNode]

  def findByNodeNameLike(name: String): Iterable[PrefNode]


}
