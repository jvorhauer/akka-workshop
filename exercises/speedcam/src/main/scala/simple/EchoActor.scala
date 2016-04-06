package simple

import akka.actor.{Actor, ActorLogging, Props}

class EchoActor extends Actor with ActorLogging {
  override def receive : Receive = {
    case msg =>
      log.info(s"receive: msg: $msg")
      sender() ! msg        // echo received message
  }
}

object EchoActor {
  val props = Props[EchoActor]
}
