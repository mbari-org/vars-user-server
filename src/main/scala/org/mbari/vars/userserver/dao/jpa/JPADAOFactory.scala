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

import javax.inject.Inject
import javax.persistence.EntityManagerFactory

import com.typesafe.config.ConfigFactory
import org.mbari.vars.userserver.dao.{ DAOFactory, PrefNodeDAO, UserDAO }
import vars.MiscDAOFactory
import vars.jpa.{ EntityManagerFactoryAspect, MiscDAOFactoryImpl, MiscFactoryImpl }

/**
 * @author Brian Schlining
 * @since 2017-06-05T15:53:00
 */
trait JPADAOFactory extends DAOFactory {

  def entityManagerFactory: EntityManagerFactory

  private[this] val miscFactory = new MiscFactoryImpl
  private[this] val miscDaoFactory = new MiscDAOFactoryImpl(entityManagerFactory)

  override def newUserDAO(): UserDAO =
    new UserDAOImpl(
      entityManagerFactory.createEntityManager(),
      miscDaoFactory,
      miscFactory
    )

  override def newPrefNodeDAO(): PrefNodeDAO =
    new PrefNodeDAOImpl(entityManagerFactory.createEntityManager())
}

class JPADAOFactoryImpl @Inject() (miscDAOFactory: MiscDAOFactory) extends JPADAOFactory {

  // HACK - This is hardcoded to VARS JPA implementation details.
  val entityManagerFactory: EntityManagerFactory =
    miscDAOFactory.asInstanceOf[EntityManagerFactoryAspect].getEntityManagerFactory
}
