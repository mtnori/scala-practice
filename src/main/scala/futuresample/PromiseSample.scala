package futuresample

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future, Promise}
import scala.language.postfixOps
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object PromiseSample extends App {
  val promiseGetInt: Promise[Int] = Promise[Int]
  val futureByPromise: Future[Int] = promiseGetInt.future // PromiseからFutureを作る

  futureByPromise.onComplete {
    case Success(i) => println(s"Success! i: ${i}")
    case Failure(t) => println(s"Failure! t: ${t.getMessage}")
  }

  Future {
    Thread.sleep(300)
    promiseGetInt.success(1)
  }

  Await.ready(futureByPromise, 5000 millisecond)
}
