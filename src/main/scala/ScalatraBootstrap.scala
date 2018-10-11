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

import java.util.concurrent.Executors
import javax.servlet.ServletContext

import org.mbari.vars.userserver.Constants
import org.mbari.vars.userserver.api.{ AuthorizationV1Api, PrefNodeV1Api, UserV1Api }
import org.mbari.vars.userserver.controllers.{ PrefNodeController, UserController }
import org.mbari.vars.userserver.dao.DAOFactory
import org.scalatra.LifeCycle
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext

/**
 *
 *
 * @author Brian Schlining
 * @since 2016-05-20T14:41:00
 */
class ScalatraBootstrap extends LifeCycle {

  private[this] val log = LoggerFactory.getLogger(getClass)


  override def init(context: ServletContext): Unit = {

    println("STARTING UP NOW")

    implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors()))

    val daoFactory: DAOFactory = Constants.DAOFactory
    val prefNodeController = new PrefNodeController(daoFactory)
    val userController = new UserController(daoFactory)

    val prefNodeApi = new PrefNodeV1Api(prefNodeController)
    val userApi = new UserV1Api(userController)
    val authApi = new AuthorizationV1Api

    context.mount(prefNodeApi, "/v1/prefs")
    context.mount(userApi, "/v1/users")
    context.mount(authApi, "/v1/auth")

  }

}
