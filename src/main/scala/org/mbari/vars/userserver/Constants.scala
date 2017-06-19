package org.mbari.vars.userserver

import com.google.inject.{ Guice, Injector }
import org.mbari.vars.userserver.dao.DAOFactory

/**
 * @author Brian Schlining
 * @since 2017-06-06T15:56:00
 */
object Constants {

  val Injector: Injector = Guice.createInjector(new InjectorModule)

  val DAOFactory: DAOFactory = Constants.Injector.getInstance(classOf[DAOFactory])
}
