//package sample.scalikejdbc
//
//import scalikejdbc._
//import org.scalatest.FlatSpec
//import sample.scalikejdbc.Sample2.Member
//import scalikejdbc.scalatest.AutoRollback
//
//class MemberSpec extends FlatSpec with AutoRollback {
//  override def fixture(implicit s: DBSession): Unit = {
//    sql"delete from members".update.apply()
//    Member.create(1, "Alice")
//  }
//
//  it should "create a new record" in { implicit s =>
//    val beforeCount = Member.count
//    Member.create(123, "Brian")
//    Member.count should equal(before + 1)
//  }
//}
