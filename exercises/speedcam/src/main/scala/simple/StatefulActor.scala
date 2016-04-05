package simple

import akka.actor.{Actor, ActorLogging}

/**
  * @author juvor
  * @since 05/04/16.
  */
class StatefulActor extends Actor with ActorLogging {

  var state = "Nog Niks"
  var count = 0

  override def receive : Receive = {
    case "Quit" => state = "Done"
    case s : String =>
      count += 1
      state = s
  }
}
