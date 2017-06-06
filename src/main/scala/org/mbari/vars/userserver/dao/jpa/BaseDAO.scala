package org.mbari.vars.userserver.dao.jpa

import javax.persistence.EntityManager

import org.mbari.vars.userserver.dao.DAO

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author Brian Schlining
  * @since 2017-06-05T16:08:00
  */
class BaseDAO(val entityManager: EntityManager) extends DAO {
  override def runTransaction[R](fn: (this.type) => R)(
      implicit ec: ExecutionContext): Future[R] = {

    import Implicits.RichEntityManager
    def fn2(em: EntityManager): R = fn.apply(this)
    entityManager.runTransaction(fn2)
  }

  override def close(): Unit = if (entityManager.isOpen) entityManager.close()
}
