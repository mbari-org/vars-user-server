val auth0Version = "3.19.1"
val circeVersion      = "0.14.1"
val codecVersion = "1.15"
val configVersion = "1.4.2"
val derbyVersion = "10.15.2.0"
val gsonJavatimeVersion = "1.1.1"
val gsonVersion = "2.9.0"
val httpcomponentsVersion = "4.5.13" // override 4.5.6 in scalatratest. It's broken
val hikariVersion = "3.4.5"
val jasyptVersion = "1.9.3"
val javamelodyVersion = "1.90.0"
val jettyVersion = "9.4.45.v20220203"
val json4sVersion = "4.0.4"
val jtaVersion = "1.1"
val junitVersion = "4.13.2"
val logbackVersion = "1.3.0-alpha14"
val oracleVersion = "19.3.0.0"
val postgresqlVersion   = "42.3.3"
val scalatestVersion = "3.2.11"
val scalatraVersion = "2.8.2"
val servletVersion = "3.1.0"
val slf4jVersion = "2.0.0-alpha7"
val sqlserverVersion = "9.4.0.jre11"
// val varskbVersion = "11.0.12"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val buildSettings = Seq(
  organization := "org.mbari",
  scalaVersion := "2.13.8",
  crossScalaVersions := Seq("2.13.8"),
  organizationName := "Monterey Bay Aquarium Research Institute",
  startYear := Some(2017),
  licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt"))
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
lazy val appSettings = buildSettings ++ dependencySettings ++
    optionSettings

lazy val apps = Map("jetty-main" -> "JettyMain")  // for sbt-pack

lazy val `vars-user-server` = (project in file("."))
  .enablePlugins(
    AutomateHeaderPlugin, 
    GitBranchPrompt, 
    GitVersioning, 
    JettyPlugin,
    PackPlugin)
  .settings(appSettings)
  .settings(
    name := "vars-user-server",
    fork := true,
    // Set version based on git tag. I use "0.0.0" format (no leading "v", which is the default)
    // Use `show gitCurrentTags` in sbt to update/see the tags
    git.gitTagToVersionNumber := { tag: String =>
      if(tag matches "[0-9]+\\..*") Some(tag)
      else None
    },
    git.useGitDescribe := true,
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
        "org.apache.httpcomponents" % "httpcomponents-client"                % httpcomponentsVersion % Test,
        "org.apache.httpcomponents" % "httpmime"                  % httpcomponentsVersion % Test,
        "org.eclipse.jetty" % "jetty-server" % jettyVersion % "compile;test",
        "org.eclipse.jetty" % "jetty-servlets" % jettyVersion % "compile;test",
        "org.eclipse.jetty" % "jetty-webapp" % jettyVersion % "compile;test",
        "org.eclipse.persistence" % "org.eclipse.persistence.jpa" % "2.7.10",
        "org.jasypt" % "jasypt" % jasyptVersion,
        "org.json4s" %% "json4s-jackson" % json4sVersion,
        "org.postgresql"                                 % "postgresql"                % postgresqlVersion,
        "org.scalatest" %% "scalatest" % scalatestVersion % "test",
        "org.scalatra" %% "scalatra" % scalatraVersion,
        "org.scalatra" %% "scalatra-json" % scalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % scalatraVersion,
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


