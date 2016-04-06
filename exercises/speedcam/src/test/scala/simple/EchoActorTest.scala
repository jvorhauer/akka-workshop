package simple

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import akka.util.Timeout
import org.scalatest.{Matchers, WordSpecLike}
import support.TerminateAfterAll

import scala.util.Success
import scala.concurrent.duration._

class EchoActorTest extends TestKit(ActorSystem("testsys"))
                       with WordSpecLike
                       with ImplicitSender
                       with Matchers
                       with TerminateAfterAll {

  "The Echo Actor" must {
    "echo the received message to me" in {
      val actor = system.actorOf(EchoActor.props, "echo-tell")
      actor ! "Hallo"
      expectMsg("Hallo")
      expectNoMsg()

      actor ! "Echo"
      actor ! "More echo"
      actor ! 42
      expectMsg("Echo")
      expectMsg("More echo")
      expectMsg(42)
      expectNoMsg()
    }

    "echo the received message if asked nicely" in {
      implicit val timeout = Timeout(5 seconds)

      val actor = TestActorRef(new EchoActor)
      val future = actor ? 42
      val Success(result : Int) = future.value.get
      result should be (42)
    }
  }
}
