package speedcam

case class PhotoMessage(id : String, photo : String, license : Option[String], speed : Int)
