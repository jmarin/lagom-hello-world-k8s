organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `lagom-hello-world-k8s` = (project in file("."))
  .aggregate(`lagom-hello-world-k8s-api`, `lagom-hello-world-k8s-impl`, `lagom-hello-world-k8s-stream-api`, `lagom-hello-world-k8s-stream-impl`)

lazy val `lagom-hello-world-k8s-api` = (project in file("lagom-hello-world-k8s-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagom-hello-world-k8s-impl` = (project in file("lagom-hello-world-k8s-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`lagom-hello-world-k8s-api`)

lazy val `lagom-hello-world-k8s-stream-api` = (project in file("lagom-hello-world-k8s-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagom-hello-world-k8s-stream-impl` = (project in file("lagom-hello-world-k8s-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`lagom-hello-world-k8s-stream-api`, `lagom-hello-world-k8s-api`)
