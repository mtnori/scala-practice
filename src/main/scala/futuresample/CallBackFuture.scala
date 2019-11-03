package futuresample

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.Random

class CallbackSomething {
  val random = new Random()

  def doSomething(onSuccess: Int => Unit,
                  onFailure: Throwable => Unit): Unit = {
    val i = random.nextInt(10)
    if (i < 5) onSuccess(i) else onFailure(new RuntimeException(i.toString))
  }
}

class FutureSomething {
  val callbackSomething = new CallbackSomething

  def doSomething(): Future[Int] = {
    val promise = Promise[Int]
    callbackSomething.doSomething(
      i => promise.success(i), // Promiseには成功/失敗した時の値を設定できる
      t => promise.failure(t)
    )
    // PromiseからFutureを作ることが出来る
    promise.future
  }
}

object CallBackFuture extends App {
  val futureSomething = new FutureSomething

  val iFuture = futureSomething.doSomething()
  val jFuture = futureSomething.doSomething()

  for {
    i <- iFuture
    j <- jFuture
  } yield println(s"$i, $j")
}
