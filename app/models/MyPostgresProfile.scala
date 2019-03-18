package models

import be.venneborg.refined.{RefinedMapping, RefinedSupport}
import com.github.tminglei.slickpg._
import slick.basic.Capability
import slick.jdbc.JdbcCapabilities

trait MyPostgresProfile
  extends ExPostgresProfile with PgArraySupport with PgDate2Support with PgRangeSupport with PgHStoreSupport
  with PgSearchSupport with PgNetSupport with PgLTreeSupport with RefinedMapping with RefinedSupport {
  def pgjson = "jsonb"
  override protected def computeCapabilities: Set[Capability] =
    super.computeCapabilities + JdbcCapabilities.insertOrUpdate

  override val api: MyAPI.type = MyAPI

  object MyAPI
    extends API with ArrayImplicits with DateTimeImplicits with Date2DateTimePlainImplicits with NetImplicits
    with LTreeImplicits with HStoreImplicits with SearchImplicits with SearchAssistants {
    implicit val strListTypeMapper: DriverJdbcType[List[String]] = new SimpleArrayJdbcType[String]("text").to(_.toList)
  }
}

object MyPostgresProfile extends MyPostgresProfile
