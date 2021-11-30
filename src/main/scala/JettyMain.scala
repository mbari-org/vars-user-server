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

import com.typesafe.config.ConfigFactory
import javax.servlet.DispatcherType
import net.bull.javamelody.{MonitoringFilter, Parameter, ReportServlet, SessionListener}
import org.eclipse.jetty.server._
import org.eclipse.jetty.servlet.FilterHolder
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener
import org.slf4j.LoggerFactory

object JettyMain {

  object conf {
    private[this] val config = ConfigFactory.load()
    val port = config.getInt("http.port")
    val stopTimeout = config.getInt("http.stop.timeout")
    val connectorIdleTimeout = config.getInt("http.connector.idle.timeout")
    val webapp = config.getString("http.webapp")
    val contextPath = config.getString("http.context.path")
  }

  def main(args: Array[String]) = {
    System.setProperty("user.timezone", "UTC")
    val s = """
    | __   ____ _ _ __ ___       _   _ ___  ___ _ __      ___  ___ _ ____   _____ _ __ 
    | \ \ / / _` | '__/ __|_____| | | / __|/ _ \ '__|____/ __|/ _ \ '__\ \ / / _ \ '__|
    |  \ V / (_| | |  \__ \_____| |_| \__ \  __/ | |_____\__ \  __/ |   \ V /  __/ |   
    |   \_/ \__,_|_|  |___/      \__,_|___/\___|_|       |___/\___|_|    \_/ \___|_| """.stripMargin
    println(s)

    val server: Server = new Server
    LoggerFactory
      .getLogger(getClass)
      .atInfo
      .log("Starting Jetty server on port {}", conf.port)

    server setStopTimeout conf.stopTimeout
    //server setDumpAfterStart true
    server setStopAtShutdown true

    val httpConfig = new HttpConfiguration()
    httpConfig setSendDateHeader true
    httpConfig setSendServerVersion false

    val connector = new NetworkTrafficServerConnector(server, new HttpConnectionFactory(httpConfig))
    connector setPort conf.port
    connector setIdleTimeout conf.connectorIdleTimeout
    server addConnector connector


    val webApp = new WebAppContext
    webApp setContextPath conf.contextPath
    webApp setResourceBase conf.webapp
    webApp setEventListeners Array(new ScalatraListener)

    // Add JavaMelody for monitoring
    webApp.addServlet(classOf[ReportServlet], "/monitoring")
    webApp.addEventListener(new SessionListener)
    val monitoringFilter = new FilterHolder(new MonitoringFilter())
    monitoringFilter.setInitParameter(Parameter.APPLICATION_NAME.getCode, conf.webapp)
    monitoringFilter.setInitParameter("authorized-users", "adminz:Cranchiidae")
    webApp.addFilter(monitoringFilter, "/*", java.util.EnumSet.of(DispatcherType.REQUEST, DispatcherType.ASYNC))

    server setHandler webApp

    server.start()
  }
}

