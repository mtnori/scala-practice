package parser.cvs

import parser._

object CTranslatableStringParser extends CParser[CTranslatableString] {
  override def parse(input: String): ParseResult[CTranslatableString] = {
    val parser = Parser.pattern("""#[A-Z_.]+""".r)

    parser.parse(input) match {
      case result: ParseOk[String] =>
        ParseOk(CTranslatableString(result.value), result.next)
      case result: ParseNg[_] =>
        ParseNg(result.message, result.next)
    }
  }
}
