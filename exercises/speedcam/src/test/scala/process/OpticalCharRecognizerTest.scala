package process

import org.scalatest._

class OpticalCharRecognizerTest extends FlatSpec with Matchers {

  "The OCR parse method" should "return Some string if id is not dividable by 7" in {
    OpticalCharRecognizer.parse(1, "Test") should be (Some("Test"))
  }

  it should "return None if id is dividable by 7" in {
    OpticalCharRecognizer.parse(7, "Not") should be (None)
  }
}
