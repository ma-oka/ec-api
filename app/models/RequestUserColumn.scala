package models

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive

final case class RequestUserColumn(userId: Option[Long Refined Positive], userName: String, userAddress: String)
