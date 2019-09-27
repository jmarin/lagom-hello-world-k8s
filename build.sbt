import com.lightbend.lagom.core.LagomVersion

organization in ThisBuild := "com.example"
version in ThisBuild := "1.0.1-SNAPSHOT"

// version in ThisBuild ~= (_.replace('+', '-'))

// dynver in ThisBuild ~= (_.replace('+', '-'))

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test
val akkaDiscovery = "com.lightbend.lagom" %% "lagom-scaladsl-akka-discovery-service-locator" % LagomVersion.current
val akkaKubernetes = "com.lightbend.akka.discovery" %% "akka-discovery-kubernetes-api" % "1.0.0"
val postgres = "org.postgresql" % "postgresql" % "42.2.8"
val h2 = "com.h2database" % "h2" % "1.4.199"

dockerBaseImage := "adoptopenjdk/openjdk8"

dockerRepository := Some("jmarin")

lagomCassandraEnabled in ThisBuild := false

lazy val `lagom-hello-world-k8s` = (project in file("."))
  .aggregate(
    `lagom-hello-world-k8s-api`,
    `lagom-hello-world-k8s-impl`,
    `lagom-hello-world-k8s-stream-api`,
    `lagom-hello-world-k8s-stream-impl`
  )

lazy val `lagom-hello-world-k8s-api` =
  (project in file("lagom-hello-world-k8s-api"))
    .settings(
      libraryDependencies ++= Seq(
        lagomScaladslApi
      )
    )

lazy val `lagom-hello-world-k8s-impl` =
  (project in file("lagom-hello-world-k8s-impl"))
    .enablePlugins(LagomScala, Cinnamon)
    .settings(
      // Enable Cinnamon during tests
      cinnamon in test := true,
      // Add a play secret to javaOptions in run in Test, so we can run Lagom forked
      javaOptions in (Test, run) += "-Dplay.http.secret.key=x",
      libraryDependencies ++= Seq(
        lagomScaladslPersistenceJdbc,
        lagomScaladslKafkaBroker,
        lagomScaladslTestKit,
        macwire,
        scalaTest,
        akkaDiscovery,
        akkaKubernetes,
        postgres,
        h2,
        // Use Coda Hale Metrics and Lagom instrumentation
        Cinnamon.library.cinnamonCHMetrics,
        Cinnamon.library.cinnamonLagom
      )
    )
    .settings(lagomForkedTestSettings)
    .dependsOn(`lagom-hello-world-k8s-api`)

lazy val `lagom-hello-world-k8s-stream-api` =
  (project in file("lagom-hello-world-k8s-stream-api"))
    .settings(
      libraryDependencies ++= Seq(
        lagomScaladslApi
      )
    )

lazy val `lagom-hello-world-k8s-stream-impl` =
  (project in file("lagom-hello-world-k8s-stream-impl"))
    .enablePlugins(LagomScala)
    .settings(
      libraryDependencies ++= Seq(
        lagomScaladslTestKit,
        macwire,
        scalaTest
      )
    )
    .dependsOn(`lagom-hello-world-k8s-stream-api`, `lagom-hello-world-k8s-api`)
