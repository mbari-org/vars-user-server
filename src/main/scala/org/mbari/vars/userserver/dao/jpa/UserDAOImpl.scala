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

package org.mbari.vars.userserver.dao.jpa

import javax.persistence.EntityManager

import org.mbari.vars.userserver.dao.UserDAO
import org.mbari.vars.userserver.model.User

import scala.jdk.CollectionConverters._
import org.mbari.kb.core.MiscDAOFactory
import org.mbari.kb.core.MiscFactory

/**
 * @author Brian Schlining
 * @since 2017-06-05T10:38:00
 */
class UserDAOImpl (
  entityManager: EntityManager,
  miscDAOFactory: MiscDAOFactory,
  miscFactory: MiscFactory
)
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
    val ua = Option(dao.findByUserName(user.username)).map(userAccount => {
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