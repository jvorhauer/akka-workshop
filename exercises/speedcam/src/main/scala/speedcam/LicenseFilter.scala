package speedcam

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

class LicenseFilter(next : ActorRef) extends Actor with ActorLogging {
  override def receive : Receive = {
    case msg : PhotoMessage =>
      if (msg.license.isDefined) {
        next ! msg
      }
  }
}

object LicenseFilter {
  def props(next : ActorRef) = Props(new LicenseFilter(next))
}
