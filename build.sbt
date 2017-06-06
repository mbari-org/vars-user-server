val auth0Version = "3.1.0"
val codecVersion = "1.10"
val configVersion = "1.3.1"
val gsonJavatimeVersion = "1.1.1"
val gsonVersion = "2.8.0"
val hikariVersion = "2.4.5"
lazy val jasyptVersion = "1.9.2"
val jettyVersion = "9.4.4.v20170414"
lazy val json4sVersion = "3.5.2"
val jtaVersion = "1.1"
val junitVersion = "4.12"
val logbackVersion = "1.1.7"
val scalatestVersion = "3.0.3"
val scalatraVersion = "2.5.0"
val servletVersion = "3.1.0"
val slf4jVersion = "1.7.21"
val varsVersion = "9.0-SNAPSHOT"


lazy val buildSettings = Seq(
  organization := "org.mbari",
  scalaVersion := "2.12.0",
  crossScalaVersions := Seq("2.12.0")
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
    "hohonuuli-bintray" at "http://dl.bintray.com/hohonuuli/maven")
)

lazy val gitHeadCommitSha =
  SettingKey[String]("git-head", "Determines the current git commit SHA")

lazy val makeVersionProperties =
  TaskKey[Seq[File]]("make-version-props", "Makes a version.properties file")

lazy val makeVersionSettings = Seq(
  gitHeadCommitSha := scala.util.Try(Process("git rev-parse HEAD").lines.head).getOrElse(""),
  makeVersionProperties := {
    val propFile = (resourceManaged in Compile).value / "version.properties"
    val content = "version=%s" format (gitHeadCommitSha.value)
    IO.write(propFile, content)
    Seq(propFile)
  },
  resourceGenerators in Compile <+= makeVersionProperties
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
    "-Yno-adapted-args",
    "-Ywarn-value-discard",
    "-Xfuture"),
  javacOptions ++= Seq("-target", "1.8", "-source", "1.8"),
  incOptions := incOptions.value.withNameHashing(true),
  updateOptions := updateOptions.value.withCachedResolution(true)
)

// --- Aliases
addCommandAlias("cleanall", ";clean;clean-files")

// --- Modules
lazy val appSettings = buildSettings ++ consoleSettings ++ dependencySettings ++
    optionSettings

lazy val apps = Seq("jetty-main")

lazy val `vars-user-server` = (project in file("."))
  .enablePlugins(JettyPlugin)
  .settings(appSettings)
  .settings(
    name := "vars-user-server",
    version := "1.0-SNAPSHOT",
    todosTags := Set("TODO", "FIXME", "WTF?"),
    fork := true,
    libraryDependencies ++= Seq(
        "com.auth0" % "java-jwt" % auth0Version,
        "com.fatboyindustrial.gson-javatime-serialisers" % "gson-javatime-serialisers" % gsonJavatimeVersion,
        "com.google.code.gson" % "gson" % gsonVersion,
        "com.zaxxer" % "HikariCP" % hikariVersion,
        "commons-codec" % "commons-codec" % codecVersion,
        "javax.servlet" % "javax.servlet-api" % servletVersion,
        "javax.transaction" % "jta" % jtaVersion,
        "org.eclipse.jetty" % "jetty-server" % jettyVersion % "compile;test",
        "org.eclipse.jetty" % "jetty-servlets" % jettyVersion % "compile;test",
        "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "compile;test",
        "org.jasypt" % "jasypt" % jasyptVersion,
        "org.json4s" %% "json4s-jackson" % json4sVersion,
        "org.mbari.vars" % "vars-jpa" % varsVersion,
        "org.scalatest" %% "scalatest" % scalatestVersion % "test",
        "org.scalatra" %% "scalatra" % scalatraVersion,
        "org.scalatra" %% "scalatra-json" % scalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % scalatraVersion,
        "org.scalatra" %% "scalatra-slf4j" % scalatraVersion,
        "org.scalatra" %% "scalatra-swagger" % scalatraVersion,
        "org.scalatra" %% "scalatra-swagger-ext" % scalatraVersion,
        "org.scalatra" %% "scalatra-scalatest" % scalatraVersion)
          .map(_.excludeAll(ExclusionRule("org.slf4j", "slf4j-jdk14"),
            ExclusionRule("org.slf4j", "slf4j-log4j12"),
            ExclusionRule("javax.servlet", "servlet-api"),
            ExclusionRule(organization = "org.scala-lang.modules"))),
    mainClass in assembly := Some("JettyMain")
  )
  .settings( // config sbt-pack
    packAutoSettings ++ Seq(
      packExtraClasspath := apps.map(_ -> Seq("${PROG_HOME}/conf")).toMap,
      packJvmOpts := apps.map(_ -> Seq("-Duser.timezone=UTC", "-Xmx4g")).toMap,
      packDuplicateJarStrategy := "latest",
      packJarNameConvention := "original"
    )
  )


