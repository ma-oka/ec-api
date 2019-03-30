package models

import java.time.OffsetDateTime

import com.github.maoka.api.Gender
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive

final case class UserColumn(userId: Option[Long Refined Positive],
                            userPassword: String,
                            userName: String,
                            userGender: String,
                            userPhoneNumber: String,
                            userZip: String,
                            userAddress: String,
                            createdTime: Option[OffsetDateTime],
                            updatedTime: Option[OffsetDateTime])
