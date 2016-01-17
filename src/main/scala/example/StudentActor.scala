package example

import akka.actor.{Actor, ActorRef}
import example.StudentMessages._
import example.TeacherMessages._

class StudentActor(teacher: ActorRef) extends Actor {
  def receive = {
    case Study => teacher ! Question("Did Pythagoras have a beard?")
    case Answer(answer) => println(s"I got answer: $answer")
  }
}
