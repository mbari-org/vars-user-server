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

package org.mbari.vars.userserver

import org.mbari.kb.jpa.knowledgebase.Factories
import org.mbari.vars.userserver.dao.DAOFactory
import org.mbari.vars.userserver.dao.jpa.JPADAOFactoryImpl

/**
 * @author Brian Schlining
 * @since 2017-06-06T15:56:00
 */
object Constants {

  val DAOFactory: DAOFactory = {
    val factories = Factories.build()
    val miscDAOFactory = factories.getMiscDAOFactory
    new JPADAOFactoryImpl(miscDAOFactory)
  }

  //val DAOFactory: DAOFactory = Constants.Injector.getInstance(classOf[DAOFactory])
}
