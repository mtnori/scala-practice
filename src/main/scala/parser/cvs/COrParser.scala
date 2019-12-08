package parser.cvs

import parser.{ParseNg, ParseOk, ParseResult}

class COrParser[T <: CValue, U <: CValue](parser1: CParser[T],
                                          parser2: CParser[U])
    extends CParser[CValue] {
  override def parse(input: String): ParseResult[CValue] = {
    parser1.parse(input) match {
      case result1: ParseOk[T] => ParseOk(result1.value, result1.next)
      case _: ParseNg[_] => {
        parser2.parse(input) match {
          case result2: ParseOk[U] => ParseOk(result2.value, result2.next)
          case result2: ParseNg[_] => ParseNg(result2.message, input)
        }
      }
    }
  }
}
