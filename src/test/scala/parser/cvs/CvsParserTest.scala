package parser.cvs

import org.scalatest.FlatSpec
import parser.ParseOk

class CvsParserTest extends FlatSpec {

  "parse" should "succeed" in {

    val parser = CvsParser

    val result2 = parser.parse(" ( '#A_IS_B', '2', '3','AB, CDE', 'A\\\'E'  ) ")
    assert(result2.isInstanceOf[ParseOk[CValue]])
    assert(
      result2
        .asInstanceOf[ParseOk[CValue]]
        .value
        .equals(
          CArray(
            List(
              CString("#A_IS_B"),
              CString("2"),
              CString("3"),
              CString("AB, CDE"),
              CString("A\'E")
            )
          )
        )
    )

    val param = result2
      .asInstanceOf[ParseOk[CValue]]
      .value
      .asInstanceOf[CArray]
      .getVal
      .head
      .asInstanceOf[CString]
      .value
    assert(param.equals("#A_IS_B"))
  }
}
