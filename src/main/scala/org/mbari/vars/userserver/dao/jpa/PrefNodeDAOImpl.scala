package org.mbari.vars.userserver.dao.jpa

import javax.inject.Inject
import javax.persistence.EntityManager

import org.mbari.vars.userserver.dao.PrefNodeDAO
import org.mbari.vars.userserver.model.PrefNode

import scala.collection.JavaConverters._
import vars.jpa.PreferenceNode

import scala.util.Try

/**
 * @author Brian Schlining
 * @since 2017-06-05T11:12:00
 */
class PrefNodeDAOImpl @Inject() (entityManager: EntityManager)
    extends BaseDAO(entityManager) with PrefNodeDAO {

  override def create(prefNode: PrefNode): Unit =
    entityManager.persist(prefNode.toPreferenceNode)

  override def update(prefNode: PrefNode): Option[PrefNode] =
    Try(PrefNode(entityManager.merge(prefNode.toPreferenceNode))).toOption

  override def delete(prefNode: PrefNode): Unit = {
    val n = find(
      "PreferenceNode.findByNodeNameAndPrefKey",
      Map("nodeName" -> prefNode.name, "prefKey" -> prefNode.key)
    )
      .headOption
    n match {
      case None => // Do nothing
      case Some(node) => entityManager.remove(node)
    }
  }

  override def findByNodeNameAndKey(nodeName: String, key: String): Option[PrefNode] =
    findByNamedQuery(
      "PreferenceNode.findByNodeNameAndPrefKey",
      Map("nodeName" -> nodeName, "prefKey" -> key)
    )
      .headOption

  /**
   * Return names of children below the node with this name
   *
   * @param name The name of the node
   * @return names of children below the node
   */
  override def findByNodeName(name: String): Iterable[PrefNode] =
    findByNamedQuery("PreferenceNode.findAllByNodeName", Map("nodeName" -> name))

  override def findByNodeNameLike(name: String): Iterable[PrefNode] =
    findByNamedQuery("PreferenceNode.findAllLikeNodeName", Map("nodeName" -> s"$name%"))

  private def findByNamedQuery(name: String, namedParams: Map[String, _]): List[PrefNode] =
    find(name, namedParams).map(PrefNode(_))

  private def find(name: String, namedParams: Map[String, _]): List[PreferenceNode] = {
    val query = entityManager.createNamedQuery(name)
    namedParams.foreach({ case (k, v) => query.setParameter(k, v) })
    val resultList = query.getResultList
    resultList.asScala
      .map(_.asInstanceOf[PreferenceNode])
      .toList
  }

}
