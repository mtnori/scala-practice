import org.scalatest.FlatSpec
import parser.{ParseNg, ParseOk, Parser}

class EOFParserTest extends FlatSpec {
  "parse" should "succeed" in {
    val parser = Parser.eof
    val result1 = parser.parse("")
    assert(result1.isInstanceOf[ParseOk[_]])
  }

  "parse" should "fail" in {
    val parser = Parser.eof
    val result1 = parser.parse("Hello")
    assert(result1.isInstanceOf[ParseNg[_]])
  }
}
