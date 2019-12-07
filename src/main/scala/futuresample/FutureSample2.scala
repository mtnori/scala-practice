package futuresample

import scala.concurrent._
import ExecutionContext.Implicits.global

object FutureSample2 extends App {
  val f = Future {
    2 / 0
  }
  for (exc <- f.failed) println(exc)

  val f2 = Future {
    4 / 2
  }
  for (exc <- f2.failed) println(exc)
}
