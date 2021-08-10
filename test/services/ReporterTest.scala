package services

import models.Listing
import org.scalatest.FunSuite

class ReporterTest extends FunSuite {
  //TODO error and limit cases


  test("average by seller type") {
    val listings: List[Listing] = Parser.parse("/listings.csv", "/contacts.csv")
    val averageMap = Reporter.averagePricePerSellerType(listings)

    //TODO implement compare function
    val averageMapAsDouble = averageMap.map {
      case (sellerType, price) => (sellerType, price.toDouble)
    }
    assert(averageMapAsDouble == Map("private" -> 26080.48, "dealer" -> 25037.33823529411764705882352941176, "other" -> 25317.76404494382022471910112359551))
  }

  test("average price") {
    val listings: List[Listing] = Parser.parse("/listings.csv", "/contacts.csv")
    val averagePrice = Reporter.averagePrice(listings)
    assert(averagePrice.toDouble == 25381.31666666666666666666666666667)
  }

  test("percentage distribution by make") {
    val listings: List[Listing] = Parser.parse("/listings.csv", "/contacts.csv")
    val percentageDistribution = Reporter.percentageDistributionByMake(listings)

    assert(
      percentageDistribution ==
        Map("BWM" -> 0.07, "Mazda" -> 0.13333333333333333, "Mercedes-Benz" -> 0.16333333333333333, "Toyota" -> 0.16, "Audi" -> 0.14, "VW" -> 0.10333333333333333, "Fiat" -> 0.09, "Renault" -> 0.14))
  }

  test("contacts per listing per month") {
    val listings: List[Listing] = Parser.parse("/listings.csv", "/contacts.csv")
    val mostContactsPerMonth =
      Reporter.mostContactedPerMonth(listings, 5)
        .map {
          case (timestamp, listings) =>
            val stats = listings.map {
              case (listing, count) => (listing.id, count)
            }
            (timestamp, stats)
        }

    assert(
      mostContactsPerMonth ==
        List(
          ("APRIL.2020", List((1181, 37), (1118, 33), (1006, 29), (1262, 28), (1123, 28))),
          ("FEBRUARY.2020", List((1271, 37), (1138, 33), (1235, 32), (1006, 32), (1250, 31))),
          ("JANUARY.2020", List((1061, 21), (1132, 18), (1250, 17), (1285, 17), (1122, 17))),
          ("JUNE.2020", List((1258, 18), (1006, 15), (1012, 14), (1271, 14), (1037, 14))),
          ("MARCH.2020", List((1061, 31), (1181, 30), (1271, 29), (1235, 29), (1258, 29))),
          ("MAY.2020", List((1204, 35), (1098, 32), (1298, 30), (1018, 29), (1132, 27)))
        )
    )
  }


}