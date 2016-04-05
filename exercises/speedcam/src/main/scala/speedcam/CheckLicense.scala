package speedcam

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import process.OpticalCharRecognizer

class CheckLicense(next : ActorRef) extends Actor with ActorLogging {

  override def receive : Receive = {
    case msg : PhotoMessage =>
      val license : Option[String] = OpticalCharRecognizer.parse(msg.id, msg.photo)
      val processed : PhotoMessage = msg.copy(license = license)
      next ! processed
  }
}

object CheckLicense {
  def props(next : ActorRef) = Props(new CheckLicense(next))
}
