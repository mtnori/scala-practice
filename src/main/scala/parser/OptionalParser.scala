package parser

class OptionalParser[T](parser: Parser[T]) extends Parser[Option[T]] {
  override def parse(input: String): ParseResult[Option[T]] = {
    parser.parse(input) match {
      case result: ParseOk[T] => ParseOk(Some(result.value), result.next)
      case _: ParseNg[_]      => ParseOk(None, input)
    }
  }
}
