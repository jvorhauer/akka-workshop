package speedcam

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{Matchers, WordSpecLike}
import support.TerminateAfterAll

class CheckLicenseTest extends TestKit(ActorSystem("testsys")) with WordSpecLike with Matchers with TerminateAfterAll {

  "A CheckLicense" must {
    "recognize first photo" in {
      val endProbe = TestProbe()
      val actor = system.actorOf(CheckLicense.props(endProbe.ref))
      val msg = PhotoMessage(1, "Test", None, 42)
      actor ! msg
      val res = endProbe.expectMsg(msg.copy(license = Some(msg.photo)))
      res.license should be (Some("Test"))
    }

    "recognize six photos" in {
      val endProbe = TestProbe()
      val actor = system.actorOf(CheckLicense.props(endProbe.ref))
      for (i <- Range(1, 7)) {
        val msg = PhotoMessage(i, s"Test-$i", None, 42)
        actor ! msg
        endProbe.expectMsg(msg.copy(license = Some(msg.photo)))
      }
      val msgNone = PhotoMessage(7, "Test-7", None, 42)
      actor ! msgNone
      endProbe.expectMsg(msgNone)
    }
  }
}
