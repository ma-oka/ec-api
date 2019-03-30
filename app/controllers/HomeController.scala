package controllers

import play.api._
import play.api.mvc._
import models._
import models.MyPostgresProfile.api._
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.refined._
import play.api.libs.circe.Circe

import scala.concurrent._

class HomeController(cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) with Circe {

  val db: DatabaseOnSlick = new DatabaseOnSlick(Database.forConfig("db"))

  def index() = Action { implicit request =>
    Ok(views.html.index())
  }

  def listItems(name: Option[String]) = Action.async { implicit request: Request[AnyContent] =>
    db.listItemsByName(name).map(result => Ok(result.asJson))
  }

  def listItemsByBodyData(userId: Long Refined Positive) = Action.async { implicit request: Request[AnyContent] =>
    db.listItemsByUserId(userId).map(a => Ok(a.asJson))
  }

  def createOrder(userId: Long Refined Positive) = Action.async(circe.json[RequestOrderColumn]) { implicit request =>
    db.createOrder(userId, request.body).map(a => Ok(a.asJson))
  }

  def createBodyData(userId: Long Refined Positive) = Action.async(circe.json[BodyDataColumn]) { implicit request =>
    db.createBody(request.body).map(a => Ok(a.asJson))
  }

  def createUser() = Action.async(circe.json[CreateUserColumn]) { implicit request =>
    db.createUser(request.body).map(a => Ok(a.asJson))
  }

  def updateUser(userId: Long Refined Positive) = Action.async(circe.json[RequestUserColumn]) { implicit request =>
    db.updateUser(userId, request.body).map(a => Ok(ResponseUserColumn(a.userId, a.userName, a.userAddress).asJson))
  }
}
