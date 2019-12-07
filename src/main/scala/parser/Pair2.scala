package parser

class Pair2[T, U](lps: Parser[T], rps: Parser[U]) extends Parser[(T, U)] {
  override def parse(input: String): ParseResult[(T, U)] = {
    lps.parse(input) match {
      case lResult: ParseOk[T] => {
        rps.parse(lResult.next) match {
          case rResult: ParseOk[U] =>
            ParseOk((lResult.value, rResult.value), rResult.next)
          case rResult: ParseNg[_] =>
            ParseNg((lResult.value, rResult.message), rResult.next)
        }
      }
      case lResult: ParseNg[_] =>
        rps.parse(lResult.next) match {
          case rResult: ParseOk[U] =>
            ParseNg((lResult.message, rResult.value), rResult.next)
          case rResult: ParseNg[_] =>
            ParseNg((lResult.message, rResult.message), rResult.next)
        }
    }
  }
}
