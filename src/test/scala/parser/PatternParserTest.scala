package parser

import org.scalatest.FlatSpec

class PatternParserTest extends FlatSpec {
  "parse" should "succeed" in {
    val parse = Parser.pattern("""#[a-zA-Z]+""".r)
    val result1 = parse.parse("#Hello, world")

    assert(result1.isInstanceOf[ParseOk[_]])
    assert(result1.asInstanceOf[ParseOk[_]].next.equals(", world"))
  }

  "parse" should "fail" in {
    val parse = Parser.pattern("""#[a-zA-Z]+""".r)
    val result1 = parse.parse("Hello, world")

    assert(result1.isInstanceOf[ParseNg[_]])
  }
}
