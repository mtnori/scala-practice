package parser

trait ParseResult[T] {}

case class ParseOk[T](value: T, next: String) extends ParseResult[T] {
  override def toString: String = "Success(" + value + ", " + next + ")"
}

case class ParseNg[T](message: T, next: String) extends ParseResult[T] {
  override def toString: String = "Success(" + message + ", " + next + ")"
}