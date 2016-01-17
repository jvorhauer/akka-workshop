package example

import akka.actor.Actor
import example.TeacherMessages._

class TeacherActor extends Actor {
  def receive = {
    case Question("Did Pythagoras have a beard?") => sender ! Answer("yes")
    case Question("How many beards did Pythagoras have?") => sender ! Answer("one")
  }
}
