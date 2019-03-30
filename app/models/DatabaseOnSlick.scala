package models

import java.time.OffsetDateTime

import MyPostgresProfile.api._
import MyPostgresProfile.mapping._
import com.typesafe.scalalogging.LazyLogging
import cats._
import cats.implicits._
import eu.timepit.refined._
import api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.{NonNegative, Positive}
import com.github.t3hnar.bcrypt._
import java.time._

import enumeratum._
import enumeratum.values.SlickValueEnumSupport

import scala.concurrent.{ExecutionContext, Future}

class DatabaseOnSlick(val db: MyPostgresProfile.backend.Database)(implicit ec: ExecutionContext) extends LazyLogging {

  class DatabaseItemsTable(tag: Tag) extends Table[ItemColumn](tag, "items") {
    def itemId: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("item_id", O.AutoInc, O.PrimaryKey)

    def itemName: Rep[String] = column[String]("item_name")

    def itemPrice: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("item_price")

    def itemQuantity: Rep[Refined[Long, NonNegative]] = column[Long Refined NonNegative]("item_quantity")

    def sizeId: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("size_id")

    def createdTime: Rep[Option[OffsetDateTime]] = column[Option[OffsetDateTime]]("created_at")

    def updatedTime: Rep[Option[OffsetDateTime]] = column[Option[OffsetDateTime]]("updated_at")

    def deletedTime: Rep[Option[OffsetDateTime]] = column[Option[OffsetDateTime]]("deleted_at")

    def * =
      (itemId.?, itemName, itemPrice, itemQuantity, sizeId, createdTime, updatedTime, deletedTime) <> (ItemColumn.tupled, ItemColumn.unapply)
  }

  class DatabaseOrdersTable(tag: Tag) extends Table[OrderColumn](tag, "orders") {
    def orderId: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("order_id", O.AutoInc, O.PrimaryKey)

    def itemId: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("item_id")

    def orderPrice: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("order_price")

    def orderQuantity: Rep[Refined[Long, NonNegative]] = column[Long Refined NonNegative]("order_quantity")

    def userId: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("user_id")

    def status: Rep[Refined[Int, NonNegative]] = column[Int Refined NonNegative]("order_status")

    def createdTime: Rep[Option[OffsetDateTime]] = column[Option[OffsetDateTime]]("created_at")

    def updatedTime: Rep[Option[OffsetDateTime]] = column[Option[OffsetDateTime]]("updated_at")

    def * =
      (orderId.?, itemId, orderPrice, orderQuantity, userId, status, createdTime, updatedTime) <> (OrderColumn.tupled, OrderColumn.unapply)
  }

  class DatabaseUsersTable(tag: Tag) extends Table[UserColumn](tag, "users") {
    def userId: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("user_id", O.AutoInc, O.PrimaryKey)

    def userPassword: Rep[String] = column[String]("user_password")

    def userName: Rep[String] = column[String]("user_name")

    def userGender: Rep[String] = column[String]("user_gender")

    def userPhoneNumber: Rep[String] = column[String]("user_phone_number")

    def userZip: Rep[String] = column[String]("user_zip")

    def userAddress: Rep[String] = column[String]("user_address")

    def createdTime: Rep[Option[OffsetDateTime]] = column[Option[OffsetDateTime]]("created_at")

    def updatedTime: Rep[Option[OffsetDateTime]] = column[Option[OffsetDateTime]]("updated_at")

    def * =
      (userId.?, userPassword, userName, userGender, userPhoneNumber, userZip, userAddress, createdTime, updatedTime) <> (UserColumn.tupled, UserColumn.unapply)
  }

  class DatabaseBodyDataTable(tag: Tag) extends Table[BodyDataColumn](tag, "body_data") {
    def bodyDataId: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("body_data_id", O.AutoInc)

    def userId: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("user_id")

    def height: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("body_height")

    def chest: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("body_chest")

    def waist: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("body_waist")

    def * = (bodyDataId.?, userId, height, chest, waist) <> (BodyDataColumn.tupled, BodyDataColumn.unapply)
  }

  class DatabaseSizeDataTable(tag: Tag) extends Table[SizeDataColumn](tag, "size_data") {
    def sizeId: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("size_id", O.AutoInc)

    def minHeight: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("min_height")

    def maxHeight: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("max_height")

    def minChest: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("min_chest")

    def maxChest: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("max_chest")

    def minWaist: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("min_waist")

    def maxWaist: Rep[Refined[Long, Positive]] = column[Long Refined Positive]("max_waist")

    def * =
      (sizeId.?, minHeight, maxHeight, minChest, maxChest, minWaist, maxWaist) <> (SizeDataColumn.tupled, SizeDataColumn.unapply)
  }

  val itemsTable = TableQuery[DatabaseItemsTable]
  val ordersTable = TableQuery[DatabaseOrdersTable]
  val usersTable = TableQuery[DatabaseUsersTable]
  val bodyDataTable = TableQuery[DatabaseBodyDataTable]
  val sizeDataTable = TableQuery[DatabaseSizeDataTable]

  //商品を検索する
  def listItemsByName(name: Option[String]): Future[Seq[ItemColumn]] =
    db.run(
      itemsTable
        .filter(
          name.fold[DatabaseItemsTable => Rep[Boolean]]((_: DatabaseItemsTable) => LiteralColumn(true))(
            name => (_: DatabaseItemsTable).itemName like s"""%${name}%"""
          )).sortBy(_.itemId).result)

  //最新の身体データを使って検索する
  def listItemsByUserId(userId: Long Refined Positive): Future[Seq[ItemColumn]] =
    db.run((for {
      bodyData <- bodyDataTable if bodyData.userId === userId
      s <- sizeDataTable
      if (s.minHeight < bodyData.height) && (s.maxHeight > bodyData.height) && (s.minChest < bodyData.chest) && (s.maxChest > bodyData.chest) && (s.minWaist < bodyData.waist) && (s.maxWaist > bodyData.waist)
      i <- itemsTable if s.sizeId === i.sizeId
    } yield i).result)

  //身体データの変遷をテーブルに保存する
  def createBody(req: BodyDataColumn): Future[BodyDataColumn] = {
    val bodyData =
      BodyDataColumn(None, req.userId, req.height, req.chest, req.waist)
    db.run(
      (bodyDataTable returning bodyDataTable
        .map(_.bodyDataId) into ((bodyData, id) => bodyData.copy(bodyDataId = Some(id)))) += bodyData)
  }

  //注文時の処理をトランザクション処理する
  def createOrder(userId: Refined[Long, Positive], req: RequestOrderColumn): Future[OrderColumn] = {
    val order =
      OrderColumn(None, req.itemId, req.itemPrice, req.itemQuantity, userId, 1, Some(OffsetDateTime.now()), None)
    db.run(databaseUpdateItems(req).flatMap(_ => databaseCreateOrders(order)).transactionally)
  }

  //注文時にitemsの在庫を減らす
  def databaseUpdateItems(request: RequestOrderColumn): DBIO[Seq[ItemColumn]] =
    itemsTable.filter(_.itemId === request.itemId).result.head.flatMap { x =>
      itemsTable
        .filter(_.itemId === request.itemId).map(_.itemQuantity)
        .update(
          refineV[NonNegative](x.itemQuantity - request.itemQuantity).leftMap(e => throw new RuntimeException(e)).merge)
        .flatMap(_ => itemsTable.filter(_.itemId === request.itemId).result)
    }

  //ordersに新規注文を作成
  def databaseCreateOrders(order: OrderColumn): DBIO[OrderColumn] =
    (ordersTable returning ordersTable.map(_.orderId) into ((order, id) => order.copy(orderId = Some(id)))) += order

  //usersのユーザー情報を更新する
  def updateUser(userId: Refined[Long, Positive], userData: RequestUserColumn): Future[UserColumn] =
    db.run(
      usersTable
        .filter(_.userId === userId).map(databaseUserData =>
          (databaseUserData.userName, databaseUserData.userAddress, databaseUserData.updatedTime))
        .update(userData.userName, userData.userAddress, Some(OffsetDateTime.now())).flatMap(_ =>
          usersTable.filter(_.userId === userData.userId).result.head))

  def createUser(req: CreateUserColumn) = {
    val salt = generateSalt
    val user = UserColumn(
      None,
      req.userPassword.bcrypt(salt),
      req.userName,
      req.userGender,
      req.userPhoneNumber,
      req.userZip,
      req.userAddress,
      Some(OffsetDateTime.now()),
      None
    )
    db.run(usersTable returning usersTable.map(_.userId) into ((user, id) => user.copy(userId = Some(id))) += user)
  }
}
