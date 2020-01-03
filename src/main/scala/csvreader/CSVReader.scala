package csvreader

import java.io.File

import scala.io.Source
import scala.util.Try

case class TestEntity(v1: Int, v2: Int)

class DataSource(file: File, delm: String) {
  // ファイルをロードし、ヘッダーとデータに分ける
  def load: Try[(Array[Option[TestEntity]])] = Try {
    val src = Source.fromFile(file, "utf-8")
    val (head, rawFields) =
      src.getLines().toArray match {

        // ヘッダーとヘッダー以外を分ける
        case x => (x.head, x.tail)
      }
    src.close()

    // ヘッダーを除いた中身を返す
    rawFields.map(f => parse(f))
  }

  private def parse(line: String): Option[TestEntity] = {
    line.split(delm).toVector.map(_.trim) match {
      case Vector(v1, v2, v3, v4) => Some(TestEntity(v2.toInt, v3.toInt))
      case _ =>
        println(s"WARNING UNKNOWN DATA FORMAT FOR LINE: $line")
        None
    }
  }

//  // カラムごとに引数として、関数の値のListを渡すことで、その関数に応じた計算処理を行う
//  def |>
//    : PartialFunction[Seq[(Seq[String]) => Double], Try[Seq[Seq[Double]]]] = {
//    case fields: Seq[(Seq[String]) => Double] if fields.nonEmpty =>
//      load.map(data => {
//
//        // 行毎にfにそった変換処理を行う
//        val convert: ((Seq[String]) => Double) => Seq[Double] =
//          (f: Seq[String] => Double) => data.map(f)
//
//        fields.map(convert)
//      })
//  }
}

object CSVReader extends App {
  final val CSV_DELM = ","

//  // ヘッダーの位置を定義
//  val LENGTH = 1
//  val WEIGHT = 2
//  val GENDER = 3
//
//  // Doubleに変換する
//  def toDouble(v: Int): Seq[String] => Double =
//    (s: Seq[String]) => s(v).toDouble
//
//  // v1(身長)とv2(体重)からBMIを計算する
//  def toBMI(v1: Int, v2: Int): Seq[String] => Double =
//    (s: Seq[String]) => s(v1).toDouble / Math.pow(s(v2).toDouble * 0.01, 2)
//
//  // markerに一致するならば1を、そうでないならば0を返す
//  def toLabel(marker: String, v: Int): Seq[String] => Double =
//    (s: Seq[String]) => if (s(v) == marker) 1 else 0
//
//  // |>に渡すための関数のSeqを作成する.この関数に沿って、カラムごとに計算処理が行われる。
//  val extractor: Seq[(Seq[String]) => Double] = toDouble(LENGTH) :: toBMI(
//    WEIGHT,
//    LENGTH
//  ) :: toLabel("男", GENDER) :: Nil

  val file = new File(
    getClass.getClassLoader.getResource("csvreader/csvData.csv").getPath
  )

  // データのロードと extractorの関数に沿ってデータを整形
//  val data = new DataSource(file = file, delm = CSV_DELM) |> extractor
  val data = new DataSource(file = file, delm = CSV_DELM)

//  data.get.foreach(_.zipWithIndex.foreach {
//    case (col, index) => println(s"$col(${index}")
//  })

  data.load.get.foreach(v => println(v.get))
}

//object CSVReader {
//  def main(args: Array[String]): Unit = {
//    val file = new File(
//      getClass.getClassLoader.getResource("csvreader/KEN_ALL.CSV").getPath
//    )
//
//    val source =
//      Source.fromFile(file, "ms932")
//
//    val lines = source.getLines()
//    lines.foreach(line => println(line))
//
//    source.close()
//  }
//}
