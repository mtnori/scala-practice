package sample.scalikejdbc

import scalikejdbc._

object Sample {
  def main(args: Array[String]): Unit = {

    // JDBCドライバとコネクションプールの初期化
    Class.forName("org.h2.Driver")
    ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")

    // REPL上のセッションプロバイダのため
    implicit val session = AutoSession

    // テーブルを作成する。JDBCと同じく、executeを利用してDDLを実行することができる
    sql"""
    create table members (
     id serial not null primary key,
     name varchar(64),
     created_at timestamp not null
    )
    """.execute.apply()

    // 初期値を挿入する
    Seq("Alice", "Bob", "Chris") foreach { name =>
      sql"insert into members (name, created_at) values (${name}, current_timestamp)".update
        .apply()
    }

    // すべてのデータをMap値として検索する
    val entities: List[Map[String, Any]] =
      sql"select * from members".map(_.toMap()).list.apply()

    // エンティティオブジェクトと抽出器を定義する
    import java.time._
    case class Member(id: Long, name: Option[String], createdAt: ZonedDateTime)
    object Member extends SQLSyntaxSupport[Member] {
      override val tableName = "members"
      def apply(rs: WrappedResultSet) = new Member(
        rs.long("id"),
        rs.stringOpt("name"),
        rs.zonedDateTime("created_at")
      )
    }

    // すべてのメンバーを検索する
    val members: List[Member] =
      sql"select * from members".map(rs => Member(rs)).list.apply()

    // Scala REPL上の貼り付けモードを使用する
    val m = Member.syntax("m")
    val name = "Alice"
    val alice: Option[Member] = withSQL {
      select.from(Member as m).where.eq(m.name, name)
    }.map(rs => Member(rs)).single.apply()
  }
}
