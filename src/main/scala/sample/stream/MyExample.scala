package sample.stream

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.stream.scaladsl._

//see also https://github.com/adamw/reactmq
object MyExample {

  def main(args: Array[String]) {
    implicit val system = ActorSystem("system")

    implicit val materializer = FlowMaterializer()

    //Short form
    /*
    Source(() => readFromLegacySource())
      .foreach( processAndSave(_))
    */

    /**
     * Long form with use of DSL to define the flow
     */
    val source = Source(() => readFromLegacySource())

    val sink = ForeachSink[String] {
     save(_)
    }

    /*
    val materialized = FlowGraph { implicit builder =>
      import FlowGraphImplicits._
      helloSource ~> helloSink
    }.run()
    */

    val flow = source via processFlow to sink

    flow.run()
  }

  def readFromLegacySource() = {
    //dummy implementation
    Iterator.continually("Hello ")
  }


  def processFlow: Flow[String, String] = {
    //dummy implementation
    Flow[String].map( process(_) )
  }

  def process(msg: String) = {
    //dummy implementation
    msg + " processed!"
  }

  def save(msg: String) = {
    //dummy implementation
    println(msg)
    Thread.sleep(2000)
  }

}
