package speedcam

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.{Matchers, WordSpecLike}
import support.TerminateAfterAll

class CompleteFilterChainTest extends TestKit(ActorSystem("testsys")) with WordSpecLike with Matchers with TerminateAfterAll {

  val endProbe = TestProbe()
  val speedFilter = system.actorOf(SpeedFilter.props(50, endProbe.ref), "speedFilter")
  val licenseFilter = system.actorOf(LicenseFilter.props(speedFilter), "licenseFilter")
  val checkLicense = system.actorOf(CheckLicense.props(licenseFilter), "checkLicense")

  "The complete filter chain" must {
    "pass on a speeding vehicle with readable license" in {
      val msg = PhotoMessage(1, "Test", None, 51)
      checkLicense ! msg
      endProbe.expectMsg(msg.copy(license = Some("Test")))
    }

    "not pass a msg without readable license" in {
      val msg = PhotoMessage(7, "Test", None, 51)
      checkLicense ! msg
      endProbe.expectNoMsg()
    }

    "not pass a msg with speed too low" in {
      val msg = PhotoMessage(1, "Test", None, 50)
      checkLicense ! msg
      endProbe.expectNoMsg()
    }
  }
}
