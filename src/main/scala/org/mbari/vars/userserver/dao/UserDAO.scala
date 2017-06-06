package org.mbari.vars.userserver.dao

import javax.inject.Inject

import org.mbari.vars.userserver.model.User
import vars.knowledgebase.KnowledgebaseDAOFactory

/**
  * @author Brian Schlining
  * @since 2017-06-05T10:37:00
  */
trait UserDAO  extends DAO {

  def create(user: User): Unit

  def update(user: User): Option[User]

  def delete(user: User): Unit

  def findByName(name: String): Option[User]

  def findAllByRole(name: String): Iterable[User]

  def findAll(): Iterable[User]
}
