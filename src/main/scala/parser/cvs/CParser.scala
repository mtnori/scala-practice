package parser.cvs

import parser.Parser

trait CParser[T <: CValue] extends Parser[T] {
  def cor[T <: CValue](parser: CParser[T]): CParser[CValue] =
    new COrParser(this, parser)
}

object CParser extends CParser[CValue]
