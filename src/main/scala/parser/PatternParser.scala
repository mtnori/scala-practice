package parser

import scala.util.matching.Regex

class PatternParser(regex: Regex) extends Parser[String] {
  override def parse(input: String): ParseResult[String] = {
    regex.findPrefixOf(input) match {
      case Some(matched) => ParseOk(matched, input.substring(matched.length))
      case _             => ParseNg("expect: " + this.regex.toString(), input)
    }
  }
}
