package parser

trait Parser[T] {
  def parse(input: String): ParseResult[T] = ???

  def many(): Parser[List[T]] = new ManyParser[T](this)
  def plus(): Parser[List[T]] = new PlusParser[T](this)
  def option(): Parser[Option[T]] = new OptionalParser[T](this)
  def or(p: Parser[T]): Parser[T] = new OrParser[T](this, p)
  def pair2[U](rps: Parser[U]): Parser[(T, U)] = new Pair2(this, rps)
  def string(literal: String): Parser[String] = new StringParser(literal)
}

object Parser extends Parser[Any] {}
