package models

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive

final case class SizeDataColumn(size_id: Option[Long Refined Positive],
                                min_height: Long Refined Positive,
                                max_height: Long Refined Positive,
                                min_chest: Long Refined Positive,
                                max_chest: Long Refined Positive,
                                min_waist: Long Refined Positive,
                                max_waist: Long Refined Positive)
