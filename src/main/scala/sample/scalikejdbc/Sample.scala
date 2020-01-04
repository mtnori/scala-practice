package sample.scalikejdbc

import sample.scalikejdbc.Sample3.Company
import scalikejdbc._
//import skinny.orm.SkinnyCRUDMapper
//import skinny.orm.feature._
//import skinny.orm.feature.associations.Association

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
    val n = "Alice"
    val alice: Option[Member] = withSQL {
      select.from(Member as m).where.eq(m.name, n)
    }.map(rs => Member(rs)).single.apply()

    val id = 123
    val name1: Option[String] = DB readOnly { session: DBSession =>
      session.single("select name from company where id = ?", id) { rs =>
        rs.string("name")
      }
    }
    val name2: Option[String] = DB readOnly { implicit session =>
      sql"select name from company where id = ${id}"
        .map(_.string("name"))
        .single
        .apply()
    }

    // トランザクション
    def addCompany(name: String)(implicit s: DBSession = AutoSession): Unit = {
      sql"insert into company values(${name})".update.apply()
    }
    def getAllNames()(implicit s: DBSession = AutoSession): List[String] = {
      sql"select name from company".map(_.string("name")).list.apply()
    }

    val names: List[String] = getAllNames() // 新しいセッションが提供される
    DB localTx { implicit session =>
      addCompany("Typesafe") // トランザクション内
      getAllNames() // トランザクション内。"Typesafe"が結果に含まれる
    }

    // SQLSyntax (sqls)
    // SQLオブジェクトとして埋め込み可能なSQLの断片
    val c: SQLSyntax = sqls"count(*)"
    val hobby: String = "Bob%"
    val query = sql"select ${c} from members where name like ${hobby}"
    // "select count(*) from members where name like ?"

    // SQLSyntaxSupport
    case class GroupMember(id: Long, fullName: Option[String] = None)
    object GroupMember extends SQLSyntaxSupport[GroupMember] {
      def apply() = ???
    }
//    create table group_member {
//      id bigint not null primary key,
//      full_name varchar(255)
//    }

  }
}

object Sample2 {
  // エンティティクラス
  case class Member(id: Long, fullName: Option[String] = None)
  // エンティティのためのコンパニオンオブジェクト
  object Member extends SQLSyntaxSupport[Member] {
    def apply(m: ResultName[Member])(rs: WrappedResultSet) =
      new Member(id = rs.get(m.id), fullName = rs.get(m.fullName))
  }

  // エンティティオブジェクトと抽出器を定義する
//  import java.time._
//  case class Member(id: Long, name: Option[String], createdAt: ZonedDateTime)
//  object Member extends SQLSyntaxSupport[Member] {
//    override val tableName = "members"
//    def apply(rs: WrappedResultSet) = new Member(
//      rs.long("id"),
//      rs.stringOpt("name"),
//      rs.zonedDateTime("created_at")
//    )
//  }

  val m = Member.syntax("m")
  val id = 123
  val member: Option[Member] = DB readOnly { implicit s =>
    sql"select ${m.result.fullName} from ${Member as m} where ${m.id} = ${id}"
      .map(Member(m.resultName))
      .single
      .apply()
  }
}

// Query DSL
object Sample3 {
  // エンティティクラス
  case class Company(id: Long, name: Option[String] = None)
  // エンティティのためのコンパニオンオブジェクト
  object Company extends SQLSyntaxSupport[Company] {
    def apply(c: ResultName[Company])(rs: WrappedResultSet) =
      new Company(id = rs.get(c.id), name = rs.get(c.name))
  }

  implicit val session = AutoSession
  val c = Company.syntax("c")
  val id = 123
  val company: Option[Company] = withSQL {
    select.from(Company as c).where.eq(c.id, id)
  }.map(Company(c.resultName)).single.apply()

  insert.into(Company).values(123, "Typesafe")
  val column = Company.column

  update(Company).set(column.name -> "Oracle").where.eq(column.id, 123)

  delete.from(Company).where.eq(column.id, 123)
}

object Sample4 {
//  // エンティティクラス
//  case class Company(id: Long, name: Option[String] = None)
//  // エンティティのためのコンパニオンオブジェクト
//  object Company extends SQLSyntaxSupport[Company] {
//    def apply(c: ResultName[Company])(rs: WrappedResultSet) =
//      new Company(id = rs.get(c.id), name = rs.get(c.name))
//  }
//  // エンティティクラス
//  case class Programmer(id: Long, name: Option[String] = None, companyId: Long)
//  // エンティティのためのコンパニオンオブジェクト
//  object Programmer extends SQLSyntaxSupport[Programmer] {
//    def apply(c: ResultName[Programmer])(rs: WrappedResultSet) =
//      new Programmer(
//        id = rs.get(c.id),
//        name = rs.get(c.name),
//        companyId = rs.get(c.companyId)
//      )
//  }
//  // エンティティクラス
//  case class ProgrammerSkill(id: Long, programmerId: Long, skillId: Long)
//  // エンティティのためのコンパニオンオブジェクト
//  object ProgrammerSkill extends SQLSyntaxSupport[ProgrammerSkill] {
//    def apply(c: ResultName[ProgrammerSkill])(rs: WrappedResultSet) =
//      new ProgrammerSkill(
//        id = rs.get(c.id),
//        programmerId = rs.get(c.programmerId),
//        skillId = rs.get(c.skillId)
//      )
//  }
//  // エンティティクラス
//  case class Skill(id: Long, name: Option[String] = None)
//  // エンティティのためのコンパニオンオブジェクト
//  object Skill extends SQLSyntaxSupport[Skill] {
//    def apply(c: ResultName[Skill])(rs: WrappedResultSet) =
//      new ProgrammerSkill(id = rs.get(c.id), name = rs.get(c.name))
//  }
//
//  val c = Company.syntax("c")
//  val p = Programmer.syntax("p")
//  val ps = ProgrammerSkill.syntax("ps")
//  val s = Skill.syntax("s")
//  val id = 123
//
//  val programmerWithSkills = withSQL {
//    select
//      .from(Programmer as p)
//      .leftJoin(Company as c)
//      .on(p.companyId, c.id)
//      .leftJoin(ProgrammerSkill as ps)
//      .on(ps.programmerId, p.id)
//      .leftJoin(Skill as s)
//      .on(ps.skillId, s.id)
//      .where
//      .eq(p.id, id)
//      .and
//      .isNull(p.deletedAt)
//  }.one(Programmer(p, c))
//    .toMany(Skill.opt(s))
//    .map { (pg, skills) =>
//      pg.copy(skills = skills)
//    }
//    .single
//    .apply()
}

//object Sample4 {
//  case class Company(id: Long, name: String)
//  object Company extends SkinnyCRUDMapper[Compans]
//
//  case class Member(id: Long,
//                    name: String,
//                    companyId: Long,
//                    company: Option[Company] = None)
//  object Member extends SkinnyCRUDMapper[Member] {
//    lazy val companyRef =
//      belongsTo[Company](Company, (m, c) => m.copy(company = c))
//    override lazy val defaultAlias = createAlias("m")
//    override def extract(rs: WrappedResultSet, n: ResultName[Member]) =
//      autoConstruct(rs, rn)
//  }
//}
