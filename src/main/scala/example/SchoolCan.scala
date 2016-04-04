package example

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{RouteResult, Route}
import akka.http.scaladsl.server.RouteResult._
import akka.io.IO
import akka.stream.ActorMaterializer
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object SchoolCan extends App {
  implicit val system = ActorSystem("SchoolSystem")
  val teacher = system.actorOf(Props[TeacherActor], "teacher")
  
  
  implicit val materializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val timeout = Timeout(5.seconds)
  val port = 8081
  val route: Route = path("hello") {
    complete("Hello!")
  } ~ path("question") {
    complete("?")
  }

  // `route` will be implicitly converted to `Flow` using `RouteResult.route2HandlerFlow`
  val bindingFuture = Http().bindAndHandle(RouteResult.route2HandlerFlow(route), "localhost", port)

  println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")
  Console.readLine()
  
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ system.terminate()) // and shutdown when done
}
