package speedcam

import akka.actor.ActorSystem
import akka.testkit._
import org.scalatest._
import support.TerminateAfterAll

class SpeedFilterTest extends TestKit(ActorSystem("testsys")) with WordSpecLike with Matchers with TerminateAfterAll {

  "A SpeedFilter" must {
    "return the msg if going too fast" in {
      val endProbe = TestProbe()
      val props = SpeedFilter.props(50, endProbe.ref)
      val actor = system.actorOf(props)
      val msg = PhotoMessage(1, "Test", None, 51)
      actor ! msg
      endProbe.expectMsg(msg)
    }

    "return no msg if not speeding" in {
      val endProbe = TestProbe()
      val props = SpeedFilter.props(50, endProbe.ref)
      val actor = system.actorOf(props)
      val msg = PhotoMessage(1, "Test", None, 50)
      actor ! msg
      endProbe.expectNoMsg()
    }
  }
}
