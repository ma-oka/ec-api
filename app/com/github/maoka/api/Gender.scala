package com.github.maoka.api

import enumeratum.values._

sealed abstract class Gender(val value: Int, name: String) extends IntEnumEntry

case object Gender extends IntPlayEnum[Gender] with IntCirceEnum[Gender] {
  val values = findValues
  case object Male extends Gender(value = 1, name = "male")
  case object Female extends Gender(value = 2, name = "female")
}
