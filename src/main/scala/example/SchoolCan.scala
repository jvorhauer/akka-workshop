package example

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import akka.pattern.ask
import spray.json.DefaultJsonProtocol

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.Success

case class QuestionInput(question: String) {
  require(question.endsWith("?"), "Questions should end in a question mark!")
}

object SchoolCan extends App with SprayJsonSupport with DefaultJsonProtocol {
  implicit val system = ActorSystem("SchoolSystem")
  val teacher = system.actorOf(Props[TeacherActor], "teacher")

  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val timeout = Timeout(5.seconds)
  val port = 8081

  implicit val questionInputFormat = jsonFormat1(QuestionInput)

  val route: Route = path("hello") {
    complete("Hello!")
  } ~ path("question") {
    post {
      entity(as[QuestionInput]) { input =>
        onComplete(teacher ? TeacherMessages.Question(input.question)) {
          case Success(TeacherMessages.Answer(answer)) => complete(200 -> answer)
          case _ => complete(500 -> "Something went horribly wrong!")
        }
      }
    }
  }

  val bindingFuture = Http().bindAndHandle(route, "localhost", port)

  println(s"Server online at http://localhost:$port/")
}
