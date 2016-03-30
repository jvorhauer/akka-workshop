package example

import akka.actor.{ActorSystem, Props}
import example.StudentMessages.Study

object SchoolApplication extends App {
  implicit val system = ActorSystem("SchoolApplication")
  val teacher = system.actorOf(Props[TeacherActor], "teacher")
  val student = system.actorOf(Props(new StudentActor2(teacher)), "student")

  student ! Study

  Thread.sleep(1000)
  system.terminate()
}
