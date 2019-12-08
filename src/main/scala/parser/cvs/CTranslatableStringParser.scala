package parser.cvs

import parser._

object CTranslatableStringParser extends CParser[CString] {
  override def parse(input: String): ParseResult[CString] = {
    val parser = Parser.string("#")

    parser.parse(input) match {
      case result: ParseOk[String] =>
        ParseOk(CString(result.value), result.next)
      case result: ParseNg[_] =>
        ParseNg(result.message, result.next)
    }
  }
}
