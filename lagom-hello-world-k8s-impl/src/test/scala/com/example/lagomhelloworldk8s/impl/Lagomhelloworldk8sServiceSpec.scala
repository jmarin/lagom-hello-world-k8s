package com.example.lagomhelloworldk8s.impl

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import com.example.lagomhelloworldk8s.api._

class Lagomhelloworldk8sServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  private val server = ServiceTest.startServer(
    ServiceTest.defaultSetup
      .withCassandra()
  ) { ctx =>
    new Lagomhelloworldk8sApplication(ctx) with LocalServiceLocator
  }

  val client: Lagomhelloworldk8sService = server.serviceClient.implement[Lagomhelloworldk8sService]

  override protected def afterAll(): Unit = server.stop()

  "lagom-hello-world-k8s service" should {

    "say hello" in {
      client.hello("Alice").invoke().map { answer =>
        answer should ===("Hello, Alice!")
      }
    }

    "allow responding with a custom message" in {
      for {
        _ <- client.useGreeting("Bob").invoke(GreetingMessage("Hi"))
        answer <- client.hello("Bob").invoke()
      } yield {
        answer should ===("Hi, Bob!")
      }
    }
  }
}
