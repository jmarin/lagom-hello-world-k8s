package com.example.lagomhelloworldk8sstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.example.lagomhelloworldk8sstream.api.Lagomhelloworldk8sStreamService
import com.example.lagomhelloworldk8s.api.Lagomhelloworldk8sService
import com.softwaremill.macwire._

class Lagomhelloworldk8sStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new Lagomhelloworldk8sStreamApplication(context) {
      override def serviceLocator: NoServiceLocator.type = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new Lagomhelloworldk8sStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[Lagomhelloworldk8sStreamService])
}

abstract class Lagomhelloworldk8sStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[Lagomhelloworldk8sStreamService](wire[Lagomhelloworldk8sStreamServiceImpl])

  // Bind the Lagomhelloworldk8sService client
  lazy val lagomhelloworldk8sService: Lagomhelloworldk8sService = serviceClient.implement[Lagomhelloworldk8sService]
}
