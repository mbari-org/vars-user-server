package org.mbari.vars.userserver.dao.jpa

import javax.inject.Inject
import javax.persistence.EntityManager

import org.mbari.vars.userserver.dao.UserDAO
import org.mbari.vars.userserver.model.User

import scala.collection.JavaConverters._
import vars.{MiscDAOFactory, MiscFactory}

/**
  * @author Brian Schlining
  * @since 2017-06-05T10:38:00
  */
class UserDAOImpl @Inject() (entityManager: EntityManager,
                             miscDAOFactory: MiscDAOFactory,
                             miscFactory: MiscFactory)
    extends BaseDAO(entityManager) with UserDAO {

  private[this] val dao = miscDAOFactory.newUserAccountDAO(entityManager)

  override def findByName(name: String): Option[User] = {
    val userAccount = dao.findByUserName(name)
    Option(userAccount).map(User(_))
  }

  override def findAllByRole(role: String): Iterable[User] = {
    val userAccounts = dao.findAllByRole(role)
    userAccounts.asScala.map(User(_))
  }

  override def findAll(): Iterable[User] = dao.findAll()
      .asScala
      .map(User(_))


  override def create(user: User): Unit = {
    val userAccount = miscFactory.newUserAccount()
    userAccount.setUserName(user.username)
    if (!user.isEncrypted) userAccount.setPassword(user.password)
    else throw new IllegalArgumentException("Attempting to create a user with previously encrypted password")
    userAccount.setRole(user.role)
    user.firstName.foreach(userAccount.setFirstName)
    user.lastName.foreach(userAccount.setLastName)
    user.affiliation.foreach(userAccount.setAffiliation)
    user.email.foreach(userAccount.setEmail)
    dao.persist(userAccount)
  }

  override def update(user: User): Option[User] = {
    val ua = Option(dao.findByUserName(user.username)).map( userAccount => {
      userAccount.setUserName(user.username)
      if (!user.isEncrypted) userAccount.setPassword(user.password)
      userAccount.setRole(user.role)
      user.firstName.foreach(userAccount.setFirstName)
      user.lastName.foreach(userAccount.setLastName)
      user.affiliation.foreach(userAccount.setAffiliation)
      user.email.foreach(userAccount.setEmail)
      userAccount
    })
    ua.foreach(u => dao.merge(u))
    ua.map(User(_))
  }

  override def delete(user: User): Unit =
    Option(dao.findByUserName(user.username)).foreach(dao.remove)

}