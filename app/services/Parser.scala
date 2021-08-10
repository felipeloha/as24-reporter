package services

import scala.io.Source
import com.github.tototoshi.csv._
import models.{Contact, Listing}

import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.Instant


object Parser {

  def parse(path: String, contactsPath: String): List[Listing] = {
    //TODO error handling for unknown path
    val source = Source.fromInputStream(getClass.getResourceAsStream(path))
    val reader = CSVReader.open(source)

    val contactsSource = Source.fromInputStream(getClass.getResourceAsStream(contactsPath))
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

    val data: Map[Int, Listing] =
      lines.foldLeft(Map[Int, Listing]()) { (acc, line) =>
        parseListing(acc, line, contactGroups)
      }


    reader.close()
    contactsReader.close()

    val listings = data.values.toList
    println(s"total listings found: ${listings.length}")
    listings
  }

  private def parseListing(acc: Map[Int, Listing], line: List[String], contactMap: Map[Int, List[Contact]]): Map[Int, Listing] = {
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
        //TODO type validation
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
