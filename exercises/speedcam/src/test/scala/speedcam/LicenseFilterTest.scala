package speedcam

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{Matchers, WordSpecLike}
import support.TerminateAfterAll

class LicenseFilterTest extends TestKit(ActorSystem("testsys")) with WordSpecLike with Matchers with TerminateAfterAll {

  "A LicenseFilter" must {
    "passes on the msg if the license was readable" in {
      val endProbe : TestProbe = TestProbe()
      val actor : ActorRef = system.actorOf(LicenseFilter.props(endProbe.ref))
      val msg : PhotoMessage = PhotoMessage(1, "Test", Some("Test"), 42)
      actor ! msg
      endProbe.expectMsg(msg)
    }

    "not send any msg if the license was unreadable" in {
      val endProbe : TestProbe = TestProbe()
      val actor : ActorRef = system.actorOf(LicenseFilter.props(endProbe.ref))
      val msg : PhotoMessage = PhotoMessage(1, "Test", None, 42)
      actor ! msg
      endProbe.expectNoMsg()
    }
  }
}
