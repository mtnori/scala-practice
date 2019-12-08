package parser.cvs

import parser._

object CStringParser extends CParser[CString] {
  override def parse(input: String): ParseResult[CString] = {
    val parser = new Pair3(
      Parser.string("'"),
      Parser.stop("'", Map("\\\'" -> "\'")),
      Parser.string("'")
    )
    parser.parse(input) match {
      case result: ParseOk[(String, String, String)] =>
        ParseOk(CString(result.value._2), result.next)
      case result: ParseNg[_] =>
        ParseNg(result.message, result.next)
    }
  }
}
