package simple

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import org.scalatest.{Matchers, WordSpecLike}
import support.TerminateAfterAll

class StatefulActorTest extends TestKit(ActorSystem("testsys")) with WordSpecLike with Matchers with TerminateAfterAll {

  "The Stateful Actor" must {
    "increase the counter after each received message" in {

      val actorRef = TestActorRef[StatefulActor]
      val actor = actorRef.underlyingActor

      actor.count should be (0)

      actorRef ! "Hallo"
      actor.count should be (1)

      actorRef ! "Is it me you are looking for?"
      actor.count should be (2)
    }

    "store the received message content in its state" in {

      val actorRef = TestActorRef[StatefulActor]
      val actor = actorRef.underlyingActor

      actor.state should be ("Nog Niks")

      actorRef ! "Hallo"
      actor.state should be ("Hallo")

      actorRef ! "Quit"
      actor.state should be ("Done")
    }
  }
}
