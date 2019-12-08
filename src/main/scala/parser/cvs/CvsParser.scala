package parser.cvs

import parser.{Pair2, ParseNg, ParseOk, ParseResult, Parser}

object CvsParser extends CParser[CValue] {
  lazy val parser = new Pair2(CValueParser, Parser.eof)

  override def parse(input: String): ParseResult[CValue] = {
    parser.parse(input) match {
      case result: ParseOk[(CValue, String)] =>
        ParseOk(result.value._1, result.next)
      case result: ParseNg[_] => ParseNg(result.message, result.next)
    }
  }
}
