package controllers

import models.Listing

import javax.inject._
import play.api.mvc._
import services.{Parser, Reporter}

import scala.io.Source

//TODO change naming

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {


  def index() = Action { implicit request: Request[AnyContent] =>

    val listings: List[Listing] = Parser.parse(
      Source.fromResource("public/listings.csv"),
      Source.fromResource("public/contacts.csv")
    )

    val averageMap: Map[String, BigDecimal] = Reporter.averagePricePerSellerType(listings)
    val averagePrice: BigDecimal = Reporter.averagePrice(listings)
    val percentageDistribution: Map[String, Double] = Reporter.percentageDistributionByMake(listings)
    val mostContactsPerMonth: Seq[(String, Seq[(Listing, Int)])] = Reporter.mostContactedPerMonth(listings, 5)

    Ok(views.html.index(averagePrice, averageMap, percentageDistribution, mostContactsPerMonth))
  }
}
