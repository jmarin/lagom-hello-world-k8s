package com.example.lagomhelloworldk8s.impl

import com.example.lagomhelloworldk8s.api
import com.example.lagomhelloworldk8s.api.Lagomhelloworldk8sService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Implementation of the Lagomhelloworldk8sService.
  */
class Lagomhelloworldk8sServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends Lagomhelloworldk8sService {

  override def hello(id: String) = ServiceCall { _ =>
    // Look up the lagom-hello-world-k8s entity for the given ID.
    val ref = persistentEntityRegistry.refFor[Lagomhelloworldk8sEntity](id)

    // Ask the entity the Hello command.
    ref.ask(Hello(id))
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the lagom-hello-world-k8s entity for the given ID.
    val ref = persistentEntityRegistry.refFor[Lagomhelloworldk8sEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }


  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(Lagomhelloworldk8sEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[Lagomhelloworldk8sEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
