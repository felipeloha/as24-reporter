package services

import models.Listing
import org.scalatest.FunSuite

//TODO error and limit cases
class ReporterTest extends FunSuite {

  test("should calculate average by seller type") {
    val listings: List[Listing] = Parser.parseFromPath("/listings.csv", "/contacts.csv")
    val averageMap = Reporter.averagePricePerSellerType(listings)

    //TODO implement compare function
    val averageMapAsDouble = averageMap.map {
      case (sellerType, price) => (sellerType, price.toDouble)
    }
    assert(averageMapAsDouble == Map("private" -> 26080.48, "dealer" -> 25037.33823529411764705882352941176, "other" -> 25317.76404494382022471910112359551))
  }

  test("should calculate average price") {
    val listings: List[Listing] = Parser.parseFromPath("/listings.csv", "/contacts.csv")
    val averagePrice = Reporter.averagePrice(listings)
    assert(averagePrice.toDouble == 25381.31666666666666666666666666667)
  }

  test("should calculate percentage distribution by make") {
    val listings: List[Listing] = Parser.parseFromPath("/listings.csv", "/contacts.csv")
    val percentageDistribution = Reporter.percentageDistributionByMake(listings)

    assert(
      percentageDistribution ==
        Map("BWM" -> 0.07, "Mazda" -> 0.13333333333333333, "Mercedes-Benz" -> 0.16333333333333333, "Toyota" -> 0.16, "Audi" -> 0.14, "VW" -> 0.10333333333333333, "Fiat" -> 0.09, "Renault" -> 0.14))
  }

  test("should calculate contacts per listing per month") {
    val listings: List[Listing] = Parser.parseFromPath("/listings.csv", "/contacts.csv")
    val TOP = 5
    //simplify the data structure
    val mostContactsPerMonth: Seq[(String, Seq[(Int, Int)])] =
      Reporter.mostContactedPerMonth(listings, TOP)
        .map {
          case (timestamp, listings) =>
            val stats = listings.map {
              case (listing, count) => (listing.id, count)
            }
            (timestamp, stats)
        }

    mostContactsPerMonth.foreach {
      case (timestamp, entries: Seq[(Int, Int)]) =>
        assert(entries.size == TOP)

        //TODO integrate scalatest to use counts shuldBe sorted
        val counts = entries.map(_._2)
        counts.zip(counts.drop(1)).foreach { case (a, b) => assert(a >= b) }
    }
  }


}