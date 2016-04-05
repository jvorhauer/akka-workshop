package process

object OpticalCharRecognizer {
  def parse(id : Int, foto : String) : Option[String] = {
    Thread.sleep(150)
    if (id % 7 != 0) {
      Some(foto)
    } else {
      None
    }
  }
}
