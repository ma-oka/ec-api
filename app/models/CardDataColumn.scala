package models

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive

final case class CardDataColumn(cardId: Long,
                                cardNumber: Long Refined Positive,
                                cardSecurityCode: Long Refined Positive)
