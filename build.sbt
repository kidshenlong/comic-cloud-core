
name := "comic-cloud-core"

organization := "io.comiccloud"

version := "0.1"

scalaVersion := "2.11.12"

val akkaVersion = "2.5.7"
val akkaHttpVersion = "10.0.11"

resolvers += Resolver.bintrayRepo("hseeberger", "maven")

libraryDependencies ++=Seq(
  "com.typesafe.akka" %%  "akka-http"                 % akkaHttpVersion,
  "com.typesafe.akka" %%  "akka-actor"                % akkaVersion,
  "com.typesafe.akka" %%  "akka-stream"               % akkaVersion,
  "com.typesafe.akka" %%  "akka-slf4j"                % akkaVersion,
  "org.slf4s"         %%  "slf4s-api"                 % "1.7.12",
  "ch.qos.logback"    %   "logback-classic"           % "1.2.1",
  "com.nulab-inc"     %%  "scala-oauth2-core"         % "1.3.0",
  "com.nulab-inc"     %%  "akka-http-oauth2-provider" % "1.3.0",
  "org.json4s"        %%  "json4s-jackson"            % "3.5.3",
  "org.json4s"        %%  "json4s-ext"                % "3.5.3",
  "de.heikoseeberger" %%  "akka-http-json4s"          % "1.17.0",
  "com.typesafe.akka" %%  "akka-http-testkit"         % akkaHttpVersion % Test,
  "com.typesafe.akka" %%  "akka-stream-testkit"       % akkaVersion     % Test,
  "org.specs2"        %%  "specs2-core"               % "4.0.0"         % Test
)

enablePlugins(DockerPlugin)

dockerfile in docker := {
  // The assembly task generates a fat JAR file
  val artifact: File = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("java")
    add(artifact, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
    expose(8080)
  }
}

imageNames in docker := Seq(
  // Sets the latest tag
  ImageName(s"${organization.value}/${name.value}:latest"),

  // Sets a name with a tag that contains the project version
  ImageName(
    namespace = Some(organization.value),
    repository = name.value,
    tag = Some("v" + version.value)
  )
)
