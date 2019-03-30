package models

import com.github.maoka.api.Gender

final case class CreateUserColumn(userPassword: String,
                                  userName: String,
                                  userGender: String,
                                  userPhoneNumber: String,
                                  userZip: String,
                                  userAddress: String)
