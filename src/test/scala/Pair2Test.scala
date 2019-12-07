import org.scalatest.FlatSpec
import parser.{ParseNg, ParseOk, Parser}

class Pair2Test extends FlatSpec {
  "parse" should "succeed" in {
    val parser = Parser.string("Hello").pair2(Parser.string("World"))
    val result1 = parser.parse("HelloWorld")
    assert(result1.isInstanceOf[ParseOk[_]])
    assert(
      result1
        .asInstanceOf[ParseOk[(String, String)]]
        .value
        .equals(("Hello", "World"))
    )

    val result2 = parser.parse("Hello")
    assert(result2.isInstanceOf[ParseNg[_]])

    val result3 = parser.parse("He")
    assert(result3.isInstanceOf[ParseNg[_]])
  }
}