package com.example.lagomhelloworldk8s.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.example.lagomhelloworldk8s.api.Lagomhelloworldk8sService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.softwaremill.macwire._
import com.lightbend.lagom.scaladsl.akka.discovery.AkkaDiscoveryComponents
import com.lightbend.lagom.scaladsl.persistence.jdbc.JdbcPersistenceComponents
import play.api.db.HikariCPComponents

class Lagomhelloworldk8sLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new Lagomhelloworldk8sApplication(context) with AkkaDiscoveryComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new Lagomhelloworldk8sApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[Lagomhelloworldk8sService])
}

abstract class Lagomhelloworldk8sApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with JdbcPersistenceComponents
    with HikariCPComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer =
    serverFor[Lagomhelloworldk8sService](wire[Lagomhelloworldk8sServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: JsonSerializerRegistry =
    Lagomhelloworldk8sSerializerRegistry

  // Register the lagom-hello-world-k8s persistent entity
  persistentEntityRegistry.register(wire[Lagomhelloworldk8sEntity])
}
