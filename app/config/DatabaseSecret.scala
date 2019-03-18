package config

final case class DatabaseSecret(url: String, driver: String, user: String, password: String)
