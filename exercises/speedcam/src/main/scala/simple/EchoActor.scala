package simple

import akka.actor.{Actor, Props}

class EchoActor extends Actor {
  override def receive : Receive = {
    case msg => sender() ! msg        // echo received message
  }
}

object EchoActor {
  val props = Props[EchoActor]
}
