package support

import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, Suite}

trait TerminateAfterAll extends BeforeAndAfterAll {
  this : TestKit with Suite =>
    override protected def afterAll(): Unit = {
      super.afterAll()
      system.terminate()
    }
}
