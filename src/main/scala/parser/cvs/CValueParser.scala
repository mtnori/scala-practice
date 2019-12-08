package parser.cvs

import parser.ParseResult

object CValueParser extends CParser[CValue] {
  lazy val parser: CParser[CValue] = CArrayParser.cor(CStringParser)
  override def parse(input: String): ParseResult[CValue] = parser.parse(input)
}
