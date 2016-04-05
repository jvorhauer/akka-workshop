package speedcam

case class PhotoMessage(id : Int, photo : String, license : Option[String], speed : Int)
