package org.mbari.vars.userserver.controllers

import org.mbari.vars.userserver.dao.{ DAO, UserDAO }

import scala.concurrent.{ ExecutionContext, Future }

/**
 * @author Brian Schlining
 * @since 2017-06-05T16:00:00
 */
trait BaseController[A <: DAO] {

  def newDAO(): A

  protected def exec[T](fn: A => T)(implicit ec: ExecutionContext): Future[T] = {
    val dao = newDAO()
    val f = dao.runTransaction(fn)
    f.onComplete(t => dao.close())
    f
  }

}
