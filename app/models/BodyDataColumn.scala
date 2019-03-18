package models

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.{NonNegative, Positive}

final case class BodyDataColumn(bodyDataId: Option[Long Refined Positive],
                                userId: Long Refined Positive,
                                height: Long Refined Positive,
                                chest: Long Refined Positive,
                                waist: Long Refined Positive)
