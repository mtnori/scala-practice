package parser.cvs

import parser.{Pair2, Pair3, ParseNg, ParseOk, ParseResult, Parser}

object CArrayParser extends CParser[CArray] {
  lazy val sp: Parser[List[String]] = Parser.string(" ").many()
  lazy val leftP = new Pair3(sp, Parser.string("("), sp)
  lazy val rightP = new Pair3(sp, Parser.string(")"), sp)

  lazy val parser: Parser[Option[
    ((List[String], CValue, List[String]),
     List[(String, (List[String], CValue, List[String]))])
  ]] = (new Pair2(
    new Pair3(sp, CValueParser, sp),
    (new Pair2(Parser.string(","), (new Pair3(sp, CValueParser, sp)))).many()
  )).option()

  override def parse(input: String): ParseResult[CArray] = {
    leftP.parse(input) match {
      case leftResult: ParseOk[(List[String], String, List[String])] => {
        parser.parse(leftResult.next) match {
          case result: ParseOk[Option[
                ((List[String], CValue, List[String]),
                List[(String, (List[String], CValue, List[String]))])
              ]] => {
            rightP.parse(result.next) match {
              case rightResult: ParseOk[(List[String], String, List[String])] => {
                var value = List.empty[CValue]
                if (result.value.isDefined) {
                  value = value :+ result.value.get._1._2
                  result.value.get._2.foreach(a => {
                    value = value :+ a._2._2
                  })
                }
                ParseOk(CArray(value), rightResult.next)
              }
              case rightResult: ParseNg[_] =>
                ParseNg(rightResult.message, rightResult.next)
            }
          }
          case result: ParseNg[_] => ParseNg(result.message, result.next)
        }
      }
      case leftResult: ParseNg[_] =>
        ParseNg(leftResult.message, leftResult.next)
    }
  }
}
