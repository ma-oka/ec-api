package models

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.{NonNegative, Positive}

final case class RequestOrderColumn(itemId: Long Refined Positive,
                                    itemName: String,
                                    itemPrice: Long Refined Positive,
                                    itemQuantity: Long Refined NonNegative)
