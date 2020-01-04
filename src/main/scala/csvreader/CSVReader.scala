package csvreader

import java.io.File
import java.nio.charset.{MalformedInputException, UnsupportedCharsetException}

import com.typesafe.scalalogging.{LazyLogging, Logger}

import scala.io.{Codec, Source}
import scala.util.Try

// Entity
trait Entity
case class TestEntity(v1: Int, v2: Int) extends Entity
case class Test2Entity(v3: String) extends Entity

trait DataSource[T <: Entity] {
  val file: File
  val deliminator: String
  val encoding: Codec

  // ファイルをロードし、ヘッダーとデータに分ける
  def load: Try[(Array[Option[T]])] = Try {
//    val src = Source.fromFile(file, encoding)
    val src = Source.fromResource("csvreader/csvData.csv")(encoding)
    val (_, rawFields) =
      src
        .getLines()
        .map(_.split(deliminator).map(_.trim).map(dropQuote).toSeq)
        .toArray match {
        // ヘッダーとヘッダー以外を分ける
        case x => (x.head, x.tail)
      }
    src.close()

    // ヘッダーを除いた中身をパースして返す
    rawFields.map(f => parse(f))
  }

  // 一行のデータをエンティティに変換する
  protected def parse(columns: Seq[String]): Option[T]

  // 前後のダブルクォーテーションを除去
  private def dropQuote(str: String): String = {
    (str.startsWith("\"") && str.endsWith("\"")) match {
      case true => (str drop 1 dropRight 1).replace("\"\"", "\"")
      case _    => str
    }
  }
}

class TestEntityDataSource(val file: File,
                           val deliminator: String,
                           val encoding: Codec = Codec.UTF8)
    extends DataSource[TestEntity]
    with LazyLogging {

  override protected def parse(columns: Seq[String]): Option[TestEntity] = {
    columns match {
      case Seq(v1, v2, v3, v4) =>
        Some(TestEntity(v2.toInt, v3.toInt))
      case _ =>
        logger.warn(s"WARNING UNKNOWN DATA FORMAT FOR LINE: $columns")
        None
    }
  }
}

class Test2EntityDataSource(val file: File,
                            val deliminator: String,
                            val encoding: Codec = Codec.UTF8)
    extends DataSource[Test2Entity]
    with LazyLogging {
  override protected def parse(columns: Seq[String]): Option[Test2Entity] = {
    columns match {
      case Seq(v1, v2, v3, v4) =>
        Some(Test2Entity(v1))
      case _ =>
        logger.warn(s"WARNING UNKNOWN DATA FORMAT FOR LINE: $columns")
        None
    }
  }
}

object CSVReader extends App with LazyLogging {
  Try {
    val deliminator = if (args.length >= 1) args(0) else ","
    val encoding = if (args.length >= 2) args(1) else "UTF-8"

    val file = new File(
      getClass.getClassLoader.getResource("csvreader/csvData.csv").getPath
    )

    val testEntityData =
      new TestEntityDataSource(
        file = file,
        deliminator = deliminator,
        encoding = encoding
      )
    val test2EntityData =
      new Test2EntityDataSource(
        file = file,
        deliminator = deliminator,
        encoding = encoding
      )

    testEntityData.load.get.foreach(v => println(v.get))
    test2EntityData.load.get.foreach(v => println(v.get))
  } recover {
    case _: UnsupportedCharsetException =>
      logger.error("Given encoding charset is unsupported.")
    case _: MalformedInputException =>
      logger.error(
        "An input byte sequence is not legal for given charset, or an input character sequence is not a legal sixteen-bit Unicode sequence."
      )
  }
}
