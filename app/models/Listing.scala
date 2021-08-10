package models

case class Listing(id: Int,
                    make: String,
                    price: BigDecimal,
                    mileage: Int,
                    sellerType: String,
                    contacts: List[Contact]
                  )