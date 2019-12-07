package parser

class PlusParser[T](parser: Parser[T]) extends Parser[List[T]] {

  override def parse(input: String): ParseResult[List[T]] = {
    var next = input
    var values = List.empty[T]
    var isContinue = true
    var result: ParseResult[T] = null
    var messages = List.empty[T]
    while (isContinue) {
      result = parser.parse(next)
      if (result.isInstanceOf[ParseOk[_]]) {
        values = values :+ result.asInstanceOf[ParseOk[T]].value
        next = result.asInstanceOf[ParseOk[T]].next
      } else {
        messages = messages :+ result.asInstanceOf[ParseNg[T]].message
        isContinue = false
      }
    }
    values.length match {
      case 0 => ParseNg(messages, input)
      case _ => ParseOk(values, next)
    }
  }
}
