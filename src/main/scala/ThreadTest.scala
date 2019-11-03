object ThreadTest {
  def main(args: Array[String]): Unit = {
    var i = 0

    new Thread(() => {
      (1 to 100).foreach(
        _ =>
          i.synchronized {
            i += 1
        }
      )
    }).start()

    new Thread(() => {
      (1 to 100).foreach(
        _ =>
          i.synchronized {
            i += 1
        }
      )
    }).start()

    println(i)
  }
}
