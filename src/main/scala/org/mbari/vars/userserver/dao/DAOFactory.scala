package org.mbari.vars.userserver.dao

/**
 * @author Brian Schlining
 * @since 2017-06-05T15:50:00
 */
trait DAOFactory {

  def newUserDAO(): UserDAO
  def newPrefNodeDAO(): PrefNodeDAO

}
