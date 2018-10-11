/*
 * Copyright 2017 Monterey Bay Aquarium Research Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mbari.vars.userserver.controllers

import org.mbari.vars.userserver.dao.{ DAOFactory, UserDAO }
import org.mbari.vars.userserver.model.User

import scala.concurrent.{ ExecutionContext, Future }

/**
 * @author Brian Schlining
 * @since 2017-06-05T14:26:00
 */
class UserController(daoFactory: DAOFactory) extends BaseController[UserDAO] {
  override def newDAO(): UserDAO = daoFactory.newUserDAO()

  def create(
    username: String,
    password: String,
    role: String,
    firstName: Option[String] = None,
    lastName: Option[String] = None,
    affiliation: Option[String] = None,
    email: Option[String] = None
  )(implicit ec: ExecutionContext): Future[User] =
    exec(d => {
      val user = User(username, password, role, affiliation, firstName, lastName, email, isEncrypted = false)
      d.create(user)
      d.findByName(username) match {
        case Some(user) => user
        case None => throw new RuntimeException(s"Failed to create user named $username")
      }
    })

  def update(
    username: String,
    role: Option[String] = None,
    firstName: Option[String] = None,
    lastName: Option[String] = None,
    affiliation: Option[String] = None,
    email: Option[String] = None
  )(implicit ec: ExecutionContext): Future[Option[User]] =
    exec(d => d.findByName(username) match {
      case None => None
      case Some(u) =>
        val user = User(username, u.password, role.getOrElse(u.role), affiliation, firstName, lastName, email)
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
