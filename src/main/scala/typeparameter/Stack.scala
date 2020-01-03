package typeparameter

trait Stack[+T] {
  def pop: (T, Stack[T])
  def push[E >: T](e: E): Stack[E]
  def isEmpty: Boolean
}

class NonEmptyStack[+T](private val top: T, private val rest: Stack[T])
    extends Stack[T] {
  def pop: (T, Stack[T]) = (top, rest)
  def push[E >: T](e: E): Stack[E] = new NonEmptyStack[E](e, this)
  def isEmpty: Boolean = false
}

case object EmptyStack extends Stack[Nothing] {
  def pop: Nothing = throw new IllegalArgumentException("empty stack")
  def push[E >: Nothing](e: E): Stack[E] = new NonEmptyStack[E](e, this)
  def isEmpty: Boolean = true
}

object Stack {
  def apply(): Stack[Nothing] = EmptyStack
}
