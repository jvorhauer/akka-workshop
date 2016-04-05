package simple

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{Matchers, WordSpecLike}
import support.TerminateAfterAll

class EchoActorTest extends TestKit(ActorSystem("testsys"))
                       with WordSpecLike
                       with ImplicitSender
                       with Matchers
                       with TerminateAfterAll {

  "The Echo Actor" must {
    "echo the received message to me" in {
      val actor = system.actorOf(EchoActor.props, "echo")
      actor ! "Hallo"
      expectMsg("Hallo")
      expectNoMsg()

      actor ! "Echo"
      actor ! "More echo"
      expectMsg("Echo")
      expectMsg("More echo")
      expectNoMsg()
    }
  }
}
