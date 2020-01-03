package typeparameter

class Cell[T](var value: T) {
  def put(newValue: T): Unit = {
    value = newValue
  }
  def get(): T = value
}

class Pair[T1, T2](val t1: T1, val t2: T2) {
  override def toString(): String = "(" + t1 + "," + t2 + ")"
}

object Pair {
  def divide(m: Int, n: Int): Pair[Int, Int] = new Pair[Int, Int](m / n, m % n)
}
