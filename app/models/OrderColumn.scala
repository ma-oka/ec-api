package models

import java.time.OffsetDateTime

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.{NonNegative, Positive}

final case class OrderColumn(orderId: Option[Long Refined Positive],
                             itemId: Long Refined Positive,
                             orderPrice: Long Refined Positive,
                             orderQuantity: Long Refined NonNegative,
                             userId: Long Refined Positive,
                             status: Int Refined NonNegative,
                             createdTime: Option[OffsetDateTime],
                             updatedTime: Option[OffsetDateTime])
