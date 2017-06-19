package org.mbari.vars.userserver.dao

import scala.concurrent.{ ExecutionContext, Future }

/**
 * @author Brian Schlining
 * @since 2017-06-05T16:06:00
 */
trait DAO {

  def runTransaction[R](fn: this.type => R)(implicit ec: ExecutionContext): Future[R]

  def close(): Unit

}
