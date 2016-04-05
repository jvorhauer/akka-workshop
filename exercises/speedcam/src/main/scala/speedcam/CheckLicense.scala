package speedcam

import akka.actor.{Actor, ActorLogging, ActorRef}
import process.OpticalCharRecognizer

class CheckLicense(next : ActorRef) extends Actor with ActorLogging {

  var count = 0

  override def receive : Receive = {
    case msg : PhotoMessage =>
      count += 1
      val license : Option[String] = OpticalCharRecognizer.parse(count, msg.photo)
      val processed : PhotoMessage = msg.copy(id = count.toString, license = license)
      next ! processed
  }
}
