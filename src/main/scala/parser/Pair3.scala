package parser

class Pair3[T, U, V](lps: Parser[T], cps: Parser[U], rps: Parser[V])
    extends Parser[(T, U, V)] {
  override def parse(input: String): ParseResult[(T, U, V)] = {
    lps.parse(input) match {
      case lResult: ParseOk[T] => {
        cps.parse(lResult.next) match {
          case cResult: ParseOk[U] => {
            rps.parse(cResult.next) match {
              case rResult: ParseOk[V] =>
                ParseOk(
                  (lResult.value, cResult.value, rResult.value),
                  rResult.next
                )
              case rResult: ParseNg[_] =>
                ParseNg(
                  (lResult.value, cResult.value, rResult.message),
                  rResult.next
                )
            }
          }
          case cResult: ParseNg[_] => {
            rps.parse(cResult.next) match {
              case rResult: ParseOk[V] =>
                ParseNg(
                  (lResult.value, cResult.message, rResult.value),
                  rResult.next
                )
              case rResult: ParseNg[_] =>
                ParseNg(
                  (lResult.value, cResult.message, rResult.message),
                  rResult.next
                )
            }
          }
        }
      }
      case lResult: ParseNg[_] =>
        cps.parse(lResult.next) match {
          case cResult: ParseOk[U] => {
            rps.parse(cResult.next) match {
              case rResult: ParseOk[V] =>
                ParseNg(
                  (lResult.message, cResult.value, rResult.value),
                  rResult.next
                )
              case rResult: ParseNg[_] =>
                ParseNg(
                  (lResult.message, cResult.value, rResult.message),
                  rResult.next
                )
            }
          }
          case cResult: ParseNg[_] => {
            rps.parse(cResult.next) match {
              case rResult: ParseOk[V] =>
                ParseNg(
                  (lResult.message, cResult.message, rResult.value),
                  rResult.next
                )
              case rResult: ParseNg[_] =>
                ParseNg(
                  (lResult.message, cResult.message, rResult.message),
                  rResult.next
                )
            }
          }
        }
    }
  }
}
