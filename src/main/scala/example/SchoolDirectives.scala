package example

import akka.http.scaladsl.server.directives.BasicDirectives
import akka.http.scaladsl.server.{Directive0, Directives}

import scala.util.Random

object SchoolDirectives extends Directives {
  def randomFailure: Directive0 = mapInnerRoute { route => ctx =>
    Random.nextBoolean() match {
      case true => route(ctx)
      case false => ctx.complete(402 -> "helaas")
    }
  }

  def randomFailure2: Directive0 = validate(Random.nextBoolean(), "helaas")

}
