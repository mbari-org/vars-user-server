package org.mbari.vars.userserver

import com.google.inject.{Binder, Module}
import org.mbari.vars.userserver.dao.DAOFactory
import org.mbari.vars.userserver.dao.jpa.JPADAOFactoryImpl

/**
  * @author Brian Schlining
  * @since 2017-06-06T15:48:00
  */
class InjectorModule extends Module {
  override def configure(binder: Binder): Unit = {
    binder.install(new vars.jpa.InjectorModule)
    binder.bind(classOf[DAOFactory]).to(classOf[JPADAOFactoryImpl])
  }
}