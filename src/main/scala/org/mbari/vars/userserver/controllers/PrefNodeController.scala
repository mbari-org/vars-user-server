package org.mbari.vars.userserver.controllers

import org.mbari.vars.userserver.dao.{DAOFactory, PrefNodeDAO}
import org.mbari.vars.userserver.model.PrefNode

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author Brian Schlining
  * @since 2017-06-05T16:17:00
  */
class PrefNodeController(daoFactory: DAOFactory) extends BaseController[PrefNodeDAO] {
  override def newDAO(): PrefNodeDAO = daoFactory.newPrefNodeDAO()

  def create(name: String, key: String, value: String)
            (implicit ec: ExecutionContext): Future[PrefNode] =
    exec(d => {
      val node = PrefNode(name, key, value)
      d.create(node)
      node
    })

  def update(name: String, key: String, value: String)
            (implicit ec: ExecutionContext): Future[PrefNode] = {
    exec(d => {
      val node = PrefNode(name, key, value)
      d.update(node)
      node
    })
  }

  def delete(name: String, key: String, value: String)
            (implicit ec: ExecutionContext): Future[Unit] =
    exec(d => d.delete(PrefNode(name, key, value)))

  def findByNodeNameAndKey(name: String, key: String)
                          (implicit ec: ExecutionContext): Future[Option[PrefNode]] =
    exec(d => d.findByNodeNameAndKey(name, key))


  def findByNodeName(name: String)
                    (implicit ec: ExecutionContext): Future[Iterable[PrefNode]] =
    exec(d => d.findByNodeName(name))


  def findByNodeNameLike(name: String)
                        (implicit ec: ExecutionContext): Future[Iterable[PrefNode]] =
    exec(d => d.findByNodeNameLike(name))




}
