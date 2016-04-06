package csv

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill, Props, Terminated}
import akka.routing.{ActorRefRoutee, FromConfig, RoundRobinPool, RoundRobinRoutingLogic, Router, SmallestMailboxPool}
import util.Database

import scala.collection.mutable
import scala.io.Source

case class Done(filename : String)
case class Count(filename : String)
case class Counted(filename : String, i : Int)
case class Line(filename : String, content : String)
case class Fields(filename : String, content : List[String])
case class Finished(filename : String)

object FileReader extends App {
  val system = ActorSystem.create("csv")
  val reader = system.actorOf(Props[FileReader], "reader")

  reader ! "NL.csv"
  reader ! "AD.csv"
}


/**
  * Read file as specified by a String message which is supposed to be a filename.
  * When finished reading a file with filename, the chain of Actors sends a total Counted message
  */
class FileReader extends Actor with ActorLogging {

  private val validator = context.actorOf(Props[LineValidator], "validator")
  private var active : Int = 0

  override def receive : Receive = {

    case filename : String =>
      active += 1
      log.info(s"start reading lines from $filename (# $active)")
      val is = this.getClass.getClassLoader.getResourceAsStream(filename)
      val lines  = Source.fromInputStream(is, "UTF-8").getLines()
      lines.foreach { l =>
        validator ! Line(filename, l)
      }
      validator ! Done(filename)

    case Counted(filename, i) =>
      log.info(s"finished reading $filename: $i lines")

    case Finished(filename) =>
      log.info(s"finished writing results of $filename")
      active -= 1
      if (active == 0) {
        Thread.sleep(500)           // a bit of extra time to finish
        context.system.terminate()
      }
  }
}


/**
  * Validate a line by splitting the contents of the received Line messages
  * in its tab-separated fields and validating all validatable fields.
  * Each valid line is passed to a persistenca Actor and counted.
  */
class LineValidator extends Actor with ActorLogging {

//  private val worker = context.actorOf(Props[Persistor])
//  private val worker = context.actorOf(SmallestMailboxPool(5).props(Props[Persistor]), "router")
  private val worker = context.actorOf(FromConfig.props(Props[Persistor]), "confrouter")
//  private val worker = context.actorOf(Props[PersistorManager], "manager")

  private val counters = mutable.Map[String, Int]()     // state is no problem within Actors

  override def receive : Receive = {
    case Line(filename, line) =>
      val fields = line.split("\\t").toList
      if (valid(fields)) {
        worker ! Fields(filename, fields)
        count(filename)
      }
    case Done(filename) =>
      worker ! Done(filename)
      sender ! Counted(filename, counters.getOrElse(filename, -1))
  }

  def valid(xs : List[String]) : Boolean = {
    xs.length == 19 && isInt(xs.head)
  }

  def isInt(s : String) : Boolean = s.matches("[0-9]+")

  def count(filename : String) : Int = {
    val c = counters.getOrElse(filename, 0)
    counters.put(filename, c + 1).getOrElse(0)
  }
}


/**
  * Store the contents of the Fields message somewhere save.
  * NB: might take some time for each 'save' action...
  */
class Persistor extends Actor with ActorLogging {

  override def receive : Receive = {
    case Fields(filename, xs) => Database.save(xs)
    case Done(filename) =>
      val reader = context.actorSelection("/user/reader")
      reader ! Finished(filename)
  }

}


/**
  * Handle the routing 'manually'
  * TODO: fix the dead letters at the end...
  */
class PersistorManager extends Actor with ActorLogging {
  var router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[Persistor])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def receive : Receive = {
    case f: Fields => router.route(f, sender())
    case d: Done => router.route(d, sender())

    case Terminated(a) =>                           // must handle supervision...
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[Persistor])
      context watch r
      router = router.addRoutee(r)
  }
}
