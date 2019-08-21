package com.example.lagomhelloworldk8sstream.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

/**
  * The lagom-hello-world-k8s stream interface.
  *
  * This describes everything that Lagom needs to know about how to serve and
  * consume the Lagomhelloworldk8sStream service.
  */
trait Lagomhelloworldk8sStreamService extends Service {

  def stream: ServiceCall[Source[String, NotUsed], Source[String, NotUsed]]

  override final def descriptor: Descriptor = {
    import Service._

    named("lagom-hello-world-k8s-stream")
      .withCalls(
        namedCall("stream", stream)
      ).withAutoAcl(true)
  }
}

