package example

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import example.StudentMessages._
import example.TeacherMessages._

class StudentActor2(teacher: ActorRef) extends Actor {
  implicit val timeout = Timeout(5.seconds) //default timeout
  import context.dispatcher                 //default executionContext

  def receive = {
    case Study =>
      (teacher ? Question("How many beards did Pythagoras have?")).onSuccess {
        case Answer(answer) => println(s"Pythagoras has $answer beard(s).")
      }
  }
}
