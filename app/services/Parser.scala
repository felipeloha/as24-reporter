package services

import scala.io.Source
import com.github.tototoshi.csv._
import models.{Contact, Listing}

import java.io.InputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.Instant


//TODO type validation
//TODO error handling for unknown path or empty files

object Parser {

  def parseFromPath(listingPath: String, contactsPath: String): List[Listing] = {
    parseFromSource(
      Source.fromInputStream(getClass.getResourceAsStream(listingPath)),
      Source.fromInputStream(getClass.getResourceAsStream(contactsPath))
    )
  }

  def parseFromSource(listing: Source, contacts: Source): List[Listing] = {
    parse(listing, contacts)
  }

  def parse(listingSource: Source, contactsSource: Source): List[Listing] = {
    val reader = CSVReader.open(listingSource)
    val contactsReader = CSVReader.open(contactsSource)

    //read lines and remove header
    val lines: List[List[String]] = reader.all().drop(1)
    val contactLines: List[List[String]] = contactsReader.all().drop(1)

    val contactGroups: Map[Int, List[Contact]] =
      contactLines
        .map {
          parseContact
        }
        .groupBy(_.listingId)

    val listings: List[Listing] =
      lines
        .foldLeft(Map[Int, Listing]()) { (acc, line) =>
          parseListing(acc, line, contactGroups)
        }
        .values
        .toList


    reader.close()
    contactsReader.close()

    println(s"total listings found: ${listings.length}")
    listings
  }

  private def parseListing(acc: Map[Int, Listing],
                           line: List[String],
                           contactMap: Map[Int, List[Contact]]): Map[Int, Listing] = {
    line match {
      case idStr :: make :: price :: mileage :: sellerType :: _ =>
        //TODO type validation
        val id: Int = idStr.toInt
        val contacts = contactMap.getOrElse(id, List())

        val listing = Listing(id, make, BigDecimal.decimal(price.toDouble), mileage.toInt, sellerType, contacts)

        acc + (id -> listing)
      case line =>
        println(s"error parsing $line to list of specific elements")
        acc
    }
  }

  private def parseContact(line: List[String]): Contact = {
    line match {
      case listingId :: date :: _ =>
        Contact(listingId.toInt, parseDate(date))
      case line =>
        println(s"error parsing $line to list of specific elements")
        null
    }
  }


  private def parseDate(millis: String): LocalDateTime = {
    LocalDateTime.ofInstant(Instant.ofEpochMilli(millis.toLong), ZoneId.systemDefault)
  }
}
