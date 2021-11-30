val auth0Version = "3.18.2"
val circeVersion      = "0.14.1"
val codecVersion = "1.15"
val configVersion = "1.4.1"
val derbyVersion = "10.15.2.0"
val gsonJavatimeVersion = "1.1.1"
val gsonVersion = "2.8.9"
val hikariVersion = "3.4.5"
val jasyptVersion = "1.9.3"
val javamelodyVersion = "1.81.0"
val jettyVersion = "9.4.44.v20210927"
val json4sVersion = "4.0.3"
val jtaVersion = "1.1"
val junitVersion = "4.13.2"
val logbackVersion = "1.3.0-alpha10"
val oracleVersion = "19.3.0.0"
val scalatestVersion = "3.2.10"
val scalatraVersion = "2.8.2"
val servletVersion = "3.1.0"
val slf4jVersion = "2.0.0-alpha5"
val sqlserverVersion = "9.4.0.jre11"
val varskbVersion = "11.0.12"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val buildSettings = Seq(
  organization := "org.mbari",
  scalaVersion := "2.13.7",
  crossScalaVersions := Seq("2.13.7"),
  organizationName := "Monterey Bay Aquarium Research Institute",
  startYear := Some(2017),
  licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))
)

lazy val consoleSettings = Seq(
  shellPrompt := { state =>
    val user = System.getProperty("user.name")
    user + "@" + Project.extract(state).currentRef.project + ":sbt> "
  },
  initialCommands in console :=
    """
      |import java.time.Instant
      |import java.util.UUID
    """.stripMargin
)

lazy val dependencySettings = Seq(
  libraryDependencies ++= {
    Seq(
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "ch.qos.logback" % "logback-core" % logbackVersion,
      "com.typesafe" % "config" % configVersion,
      "junit" % "junit" % junitVersion % "test",
      "org.scalatest" %% "scalatest" % scalatestVersion % "test",
      "org.slf4j" % "log4j-over-slf4j" % slf4jVersion,
      "org.slf4j" % "slf4j-api" % slf4jVersion)
  },
  resolvers ++= Seq(
    Resolver.mavenLocal,
    Resolver.sonatypeRepo("releases"),
    Resolver.githubPackages("mbari-org")
  )
)


lazy val optionSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8", // yes, this is 2 args
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    //"-Xfatal-warnings",
    "-Xlint",
    "-Xlint:-byname-implicit",
    "-Ywarn-value-discard"),
  javacOptions ++= Seq("-target", "17", "-source", "17"),
  updateOptions := updateOptions.value.withCachedResolution(true)
)

// --- Aliases
addCommandAlias("cleanall", ";clean;clean-files")

// --- Modules
lazy val appSettings = buildSettings ++ consoleSettings ++ dependencySettings ++
    optionSettings

lazy val apps = Map("jetty-main" -> "JettyMain")  // for sbt-pack

lazy val `vars-user-server` = (project in file("."))
  .enablePlugins(JettyPlugin)
  .enablePlugins(AutomateHeaderPlugin)
  .enablePlugins(PackPlugin)
  .settings(appSettings)
  .settings(
    name := "vars-user-server",
    version := "0.2.4",
    fork := true,
    libraryDependencies ++= Seq(
        "com.auth0" % "java-jwt" % auth0Version,
        "com.fatboyindustrial.gson-javatime-serialisers" % "gson-javatime-serialisers" % gsonJavatimeVersion,
        "com.google.code.gson" % "gson" % gsonVersion,
        "com.microsoft.sqlserver" % "mssql-jdbc" % sqlserverVersion,
        "com.oracle.ojdbc" % "ojdbc8" % oracleVersion,
        "com.zaxxer" % "HikariCP" % hikariVersion,
        "commons-codec" % "commons-codec" % codecVersion,
        "io.circe"                                       %% "circe-core"               % circeVersion,
        "io.circe"                                       %% "circe-generic"            % circeVersion,
        "io.circe"                                       %% "circe-parser"             % circeVersion,
        "javax.servlet" % "javax.servlet-api" % servletVersion,
        "javax.transaction" % "jta" % jtaVersion,
        "net.bull.javamelody" % "javamelody-core" % javamelodyVersion,
        "org.apache.derby" % "derby" % derbyVersion, //          % "test",
        "org.apache.derby" % "derbyclient" % derbyVersion, //          % "test",
        "org.apache.derby" % "derbynet" % derbyVersion, //
        "org.apache.derby" % "derbytools" % derbyVersion, //
        "org.apache.derby" % "derbyshared" % derbyVersion, //
        "org.eclipse.jetty" % "jetty-server" % jettyVersion % "compile;test",
        "org.eclipse.jetty" % "jetty-servlets" % jettyVersion % "compile;test",
        "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "compile;test",
        "org.jasypt" % "jasypt" % jasyptVersion,
        "org.json4s" %% "json4s-jackson" % json4sVersion,
        "org.mbari.vars"               % "org.mbari.kb.jpa"          % varskbVersion,
        "org.scalatest" %% "scalatest" % scalatestVersion % "test",
        "org.scalatra" %% "scalatra" % scalatraVersion,
        "org.scalatra" %% "scalatra-json" % scalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % scalatraVersion,
        //"org.scalatra" %% "scalatra-slf4j" % scalatraVersion,
        //"org.scalatra" %% "scalatra-swagger" % scalatraVersion,
        //"org.scalatra" %% "scalatra-swagger-ext" % scalatraVersion,
        "org.scalatra" %% "scalatra-scalatest" % scalatraVersion)
          .map(_.excludeAll(ExclusionRule("org.slf4j", "slf4j-jdk14"),
            ExclusionRule("org.slf4j", "slf4j-log4j12"),
            ExclusionRule("javax.servlet", "servlet-api")))
  )
  .settings( // config sbt-pack
    packMain := apps,
    packExtraClasspath := apps.keys.map(k => k -> Seq("${PROG_HOME}/conf")).toMap,
    packJvmOpts := apps.keys.map(k => k -> Seq("-Duser.timezone=UTC", "-Xmx4g")).toMap,
    packDuplicateJarStrategy := "latest",
    packJarNameConvention := "original"
  )


