package speedcam

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

class SpeedFilter(maxSpeed : Int, next : ActorRef) extends Actor with ActorLogging {

  override def receive : Receive = {
    case msg : PhotoMessage =>
      if (msg.speed > maxSpeed) {
        next ! msg                  // too fast, so proceed in the filter chain
      }
  }
}

object SpeedFilter {
  def props(maxSpeed : Int, next : ActorRef) = Props(new SpeedFilter(maxSpeed, next))
}
