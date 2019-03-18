package models

import java.time.OffsetDateTime

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

final case class ItemColumn(itemId: Option[Long Refined Positive],
                            itemName: String,
                            itemPrice: Long Refined Positive,
                            itemQuantity: Long Refined NonNegative,
                            sizeId: Long Refined Positive,
                            createTime: Option[OffsetDateTime],
                            updateTime: Option[OffsetDateTime],
                            deleteTime: Option[OffsetDateTime])
