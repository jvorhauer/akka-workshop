package csv

import akka.actor.{Actor, ActorLogging, ActorSystem, PoisonPill, Props}

import scala.io.Source

case object Done
case object Count
case class Counted(i : Int)

object FileReader extends App {
  val system = ActorSystem.create("csv")
  val reader = system.actorOf(Props[FileReader])

//  reader ! "AD.csv"
  reader ! "NL.csv"
}

class FileReader extends Actor with ActorLogging {

  val validator = context.actorOf(Props[LineValidator])
  var name = ""

  override def receive : Receive = {

    case filename : String =>
      name = filename
      log.info(s"read lines from $filename")
      val is = this.getClass.getClassLoader.getResourceAsStream(filename)
      val lines  = Source.fromInputStream(is, "UTF-8").getLines()
      lines.foreach { l =>
        log.debug(s"line: $l")
        validator ! l
      }
      validator ! Done
    case Counted(i) =>
      log.info(s"finished reading $name: $i lines")
      context.system.terminate()
  }
}


class LineValidator extends Actor with ActorLogging {

  val persistor = context.actorOf(Props[Persistor])

  override def receive : Receive = {
    case line : String =>
      val fields = line.split("\\t").toList
      if (valid(fields)) {
        persistor ! fields
      }
    case Done => persistor ! Done
    case Counted(i) => context.parent ! Counted(i)
  }

  def valid(xs : List[String]) : Boolean = {
    xs.length == 19 && isInt(xs.head)
  }

  def isInt(s : String) : Boolean = s.matches("[0-9]+")
}


class Persistor extends Actor with ActorLogging {

  var count = 0

  override def receive : Receive = {
    case xs : List[_] => count += 1
    case Done => sender ! Counted(count)
  }
}
