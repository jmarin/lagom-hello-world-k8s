package com.example.lagomhelloworldk8sstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.example.lagomhelloworldk8sstream.api.Lagomhelloworldk8sStreamService
import com.example.lagomhelloworldk8s.api.Lagomhelloworldk8sService

import scala.concurrent.Future

/**
  * Implementation of the Lagomhelloworldk8sStreamService.
  */
class Lagomhelloworldk8sStreamServiceImpl(lagomhelloworldk8sService: Lagomhelloworldk8sService) extends Lagomhelloworldk8sStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(lagomhelloworldk8sService.hello(_).invoke()))
  }
}
