package org.mbari.vars.userserver

import com.google.inject.Guice

/**
  * @author Brian Schlining
  * @since 2017-06-06T15:56:00
  */
object Constants {

  val Injector = Guice.createInjector(new InjectorModule)
}
