package services

import models.{Contact, Listing}

object Reporter {

  //TODO make all functions more generic


  def averagePricePerSellerType(listings: List[Listing]): Map[String, BigDecimal] = {
    listings
      .groupBy(_.sellerType)
      .map {
        case (sellerType, groupedListings) =>
          val average = groupedListings.map(_.price).sum / groupedListings.size
          (sellerType, average)
      }
  }

  def percentageDistributionByMake(listings: List[Listing]): Map[String, Double] = {
    val totalListings = listings.size
    listings
      .groupBy(_.make)
      .map {
        case (make, groupedListings) =>
          val distribution = groupedListings.size.toDouble / totalListings.toDouble
          (make, distribution)
      }
  }

  def averagePrice(listings: List[Listing]): BigDecimal = {
    val sum = listings
      .map {
        _.price
      }
      .sum

    sum / listings.size
  }

  //TODO sort keys by month and year
  def mostContactedPerMonth(listings: List[Listing], top: Int): Seq[(String, Seq[(Listing, Int)])] = {
    listings
      .flatMap(_.contacts)
      //group contacts by timestamp
      .groupBy(contact => {
        s"${contact.date.getMonth}.${contact.date.getYear}"
      })
      .map {
        case (timestamp, groupedContactsByTimestamp) =>
          val contactCountPerListing =
            countContactsByListing(groupedContactsByTimestamp, listings)
              .take(top)

          (timestamp, contactCountPerListing)
      }
      .toSeq
      .sortBy(_._1)
  }

  private def countContactsByListing(groupedContactsByTimestamp: List[Contact], listings: List[Listing]) = {
    groupedContactsByTimestamp
      .groupBy(_.listingId)
      .map {
        case (listingId, contacts) =>
          val listing = listings.find(_.id == listingId).last
          (listing, contacts.size)
      }
      .toSeq
      .sortWith(_._2 > _._2)
  }
}
