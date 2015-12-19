import java.io.PrintStream
import java.net.Socket

import scala.util.{Failure, Success, Try}

object DiscardClient {
  def main(args: Array[String]): Unit = {
    val result = Try {
      val socket = new Socket("localhost", 8080)
      val out = new PrintStream(socket.getOutputStream())
      out.println("This text will be discarded.")
      Console.println("Sent.")
    }
    result match {
      case Success(v) =>
      case Failure(e) =>
        Console.println(e)
    }
  }
}