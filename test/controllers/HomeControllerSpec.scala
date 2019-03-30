package controllers

import apps.AppComponents
import models._
import MyPostgresProfile.api._
import MyPostgresProfile.mapping._
import com.github.maoka.api.Gender
import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.{NonNegative, Positive}
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.refined._
import org.scalatestplus.play._
import org.scalatestplus.play.components.OneAppPerSuiteWithComponents
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Future

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  *
  * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
  */
class HomeControllerSpec
  extends PlaySpec with Results with OneAppPerSuiteWithComponents with FutureAwaits with DefaultAwaitTimeout {

  val db: MyPostgresProfile.backend.Database = Database.forConfig("db")

  override def components: AppComponents = new AppComponents(context)

  "HomeController GET" should {

    "render the index page from a new instance of controller" in {
      val controller = components.applicationController
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }

    "render the index page from the application" in {
      val controller = components.applicationController
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }

    "ReadAPI" in {
      val controller = components.applicationController
      val (itemId, tableItemQuantity) =
        (refineV[Positive](2.toLong).right.get, refineV[NonNegative](150.toLong).right.get)
      await(
        db.run(new DatabaseOnSlick(db)(components.executionContext).itemsTable
          .filter(_.itemId === itemId).map(_.itemQuantity).update(tableItemQuantity)))
      val result: Future[Result] = controller.listItems(Some("シャツ"))(FakeRequest(GET, "/items?name=シャツ"))
      val bodyText = contentAsString(result)
      bodyText mustBe
        """[{"itemId":1,"itemName":"シャツ","itemPrice":1000,"itemQuantity":100,"sizeId":1,"createTime":null,"updateTime":null,"deleteTime":null},{"itemId":2,"itemName":"シャツ","itemPrice":900,"itemQuantity":150,"sizeId":2,"createTime":null,"updateTime":null,"deleteTime":null}]"""
    }
  }

  "HomeController POST" should {
    "readUserBodyDataItems" in {
      val controller = components.applicationController
      val result =
        controller.listItemsByBodyData(refineV[Positive](2.toLong).right.get)(FakeRequest(GET, "/items?user=2"))
      val bodyText = contentAsString(result)
      bodyText mustBe """[{"itemId":1,"itemName":"シャツ","itemPrice":1000,"itemQuantity":100,"sizeId":1,"createTime":null,"updateTime":null,"deleteTime":null}]"""
    }

    "updateUsers" in {
      val (testUserId, testUserName, testUserAddress, userId, userName, userAddress) =
        (refineV[Positive](3.toLong).right.get,
         "okita",
         "kyoto",
         refineV[Positive](3.toLong).right.get,
         "yamada",
         "osaka")
      await(
        db.run(
          new DatabaseOnSlick(db)(components.executionContext).usersTable
            .filter(_.userId === testUserId).map(databaseUserData =>
              (databaseUserData.userName, databaseUserData.userAddress))
            .update(testUserName, testUserAddress)))
      val controller = components.applicationController
      implicit val mat = components.application.materializer
      val body = parse(s"""{"userId":${userId},"userName":"${userName}","userAddress":"${userAddress}"}""")
        .flatMap(_.as[RequestUserColumn]).right.get
      val result =
        controller.updateUser(refineV[Positive](3.toLong).right.get)(FakeRequest(POST, "/users").withBody(body))
      val bodyText = contentAsString(result)
      bodyText mustBe """{"userId":3,"userName":"yamada","userAddress":"osaka"}"""
    }

    "createOrders" in {
      val (itemId, itemName, itemPrice, itemQuantity, userId, tableItemQuantity, orderStatus) =
        (refineV[Positive](2.toLong).right.get,
         "シャツ",
         refineV[Positive](900.toLong).right.get,
         refineV[Positive](1.toLong).right.get,
         refineV[Positive](2.toLong).right.get,
         refineV[NonNegative](150.toLong).right.get,
         refineV[Positive](1.toLong).right.get)
      await(
        db.run(new DatabaseOnSlick(db)(components.executionContext).itemsTable
          .filter(_.itemId === itemId).map(_.itemQuantity).update(tableItemQuantity)))
      val controller = components.applicationController
      implicit val mat = components.application.materializer
      val body =
        parse(s"""{"itemId":${itemId},"itemName":"${itemName}","itemPrice":${itemPrice},
           |"itemQuantity":${itemQuantity}}""".stripMargin).flatMap(_.as[RequestOrderColumn]).right.get
      val result =
        controller.createOrder(refineV[Positive](2.toLong).right.get)(FakeRequest(POST, "/orders").withBody(body))
      val bodyText: String = contentAsString(result)
      println(bodyText)
      bodyText mustBe s"""{"orderId":${parse(bodyText)
        .flatMap(_.as[OrderColumn]).right.get.orderId.get},"itemId":${itemId},"orderPrice":${itemPrice},"orderQuantity":${itemQuantity},"userId":${userId},"status":${orderStatus},"createdTime":"${parse(
        bodyText).flatMap(_.as[OrderColumn]).right.get.createdTime.get}","updatedTime":null}"""
      db.run(
        new DatabaseOnSlick(db)(components.executionContext).ordersTable
          .filter(_.orderId === parse(bodyText).flatMap(_.as[OrderColumn]).right.get.orderId.get).delete)
    }

    "cretaeBodyData" in {
      val (bodyDataId, userId, height, chest, waist) =
        (refineV[Positive](1.toLong).right.get,
         refineV[Positive](1.toLong).right.get,
         refineV[Positive](1650.toLong).right.get,
         refineV[Positive](820.toLong).right.get,
         refineV[Positive](700.toLong).right.get)
      val controller = components.applicationController
      implicit val mat = components.application.materializer
      await(
        db.run(new DatabaseOnSlick(db)(components.executionContext).bodyDataTable.filter(_.userId === userId).delete))
      val body =
        parse(
          s"""{"bodyDataId":${bodyDataId},"userId":${userId},"height":${height},"chest":${chest},"waist":${waist}}""")
          .flatMap(_.as[BodyDataColumn]).right.get
      val result =
        controller.createBodyData(refineV[Positive](1.toLong).right.get)(FakeRequest(POST, "/bodyData").withBody(body))
      val bodyText = contentAsString(result)
      bodyText mustBe s"""{"bodyDataId":${parse(bodyText)
        .flatMap(_.as[BodyDataColumn]).right.get.bodyDataId.get},"userId":1,"height":1650,"chest":820,"waist":700}"""
    }

    "createUser" in {
      val controller = components.applicationController
      implicit val mat = components.application.materializer
      val body =
        parse(
          s"""{"userPassword":"USPb9mhu","userName":"hayashi","userGender":"male","userPhoneNumber":"09087654321","userZip":"1002000","userAddress":"tokyo"}""")
          .flatMap(_.as[CreateUserColumn]).right.get
      val result = controller.createUser()(FakeRequest(POST, "/users").withBody(body))
      val bodyText = contentAsString(result)
      await(
        db.run(new DatabaseOnSlick(db)(components.executionContext).usersTable
          .filter(_.userId === parse(bodyText).flatMap(_.as[UserColumn]).right.get.userId.get).delete))
      bodyText mustBe s"""{"userId":${parse(bodyText)
        .flatMap(_.as[UserColumn]).right.get.userId.get},"userPassword":"${parse(bodyText)
        .flatMap(_.as[UserColumn]).right.get.userPassword}","userName":"hayashi","userGender":"male","userPhoneNumber":"09087654321","userZip":"1002000","userAddress":"tokyo","createdTime":"${parse(
        bodyText).flatMap(_.as[UserColumn]).right.get.createdTime.get}","updatedTime":null}"""
    }
  }
}
