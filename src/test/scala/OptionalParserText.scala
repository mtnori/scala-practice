import org.scalatest.FlatSpec
import parser.{ParseOk, Parser}

class OptionalParserText extends FlatSpec {
  "parse" should "succeed" in {
    val parser = Parser.string("Hello").option()
    val result1 = parser.parse("Hello, World")
    assert(result1.isInstanceOf[ParseOk[_]])
    assert(
      result1.asInstanceOf[ParseOk[Option[String]]].value.contains("Hello")
    )

    val result2 = parser.parse("Hell, World")
    assert(result2.isInstanceOf[ParseOk[_]])
    assert(result2.asInstanceOf[ParseOk[Option[String]]].value.isEmpty)
  }

}
