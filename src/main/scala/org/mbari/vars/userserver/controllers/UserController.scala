package org.mbari.vars.userserver.controllers

import org.mbari.vars.userserver.dao.{DAOFactory, UserDAO}
import org.mbari.vars.userserver.model.User

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author Brian Schlining
  * @since 2017-06-05T14:26:00
  */
class UserController(daoFactory: DAOFactory) extends BaseController[UserDAO] {
  override def newDAO(): UserDAO = daoFactory.newUserDAO()

  def create(username: String,
             password: String,
             role: String,
             firstName: Option[String] = None,
             lastName: Option[String] = None,
             affiliation: Option[String] = None,
             email: Option[String] = None)(implicit ec: ExecutionContext): Future[User] =
    exec(d => {
      val user = User(username, password, role, affiliation, firstName, lastName, email)
      d.create(user)
      d.findByName(username) match {
        case Some(user) => user
        case None => throw new RuntimeException(s"Failed to create user named $username")
      }
    })

  def update(username: String,
             password: String,
             role: String,
             firstName: Option[String] = None,
             lastName: Option[String] = None,
             affiliation: Option[String] = None,
             email: Option[String] = None)(implicit ec: ExecutionContext): Future[Option[User]] =
    exec(d => {
      val user = User(username, password, role, affiliation, firstName, lastName, email)
      d.update(user)
    })

  def delete(username: String)(implicit ec: ExecutionContext): Future[Unit] =
    exec(d => d.delete(User(username, "")))

  def findAll(implicit ec: ExecutionContext): Future[Iterable[User]] =
    exec(d => d.findAll())

  def findAllByRole(role: String)(implicit ec: ExecutionContext): Future[Iterable[User]] =
    exec(d => d.findAllByRole(role))

  def findByName(name: String)(implicit ec: ExecutionContext): Future[Option[User]] =
    exec(d => d.findByName(name))
}
