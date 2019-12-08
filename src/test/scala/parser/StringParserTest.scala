package parser

import org.scalatest.FlatSpec

class StringParserTest extends FlatSpec {
  "parse" should "succeed" in {
    val parse = Parser.string("Hello")
    val result1 = parse.parse("Hello, world")

    assert(result1.isInstanceOf[ParseOk[_]])
    assert(result1.asInstanceOf[ParseOk[_]].next.equals(", world"))
  }

  "parse" should "fail" in {
    val parse = Parser.string("Hello")
    val result1 = parse.parse("hello, world")

    assert(result1.isInstanceOf[ParseNg[_]])
  }
}
