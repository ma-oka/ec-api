package config

import pureconfig._
import pureconfig.generic.auto._
import pureconfig.{CamelCase, ConfigFieldMapping}
import pureconfig.generic.ProductHint

final case class ApplicationConfig(db: DatabaseSecret)

object ApplicationConfig {
  implicit def hint[T]: ProductHint[T] =
    ProductHint[T](ConfigFieldMapping(CamelCase, CamelCase))
  def loadConfig: ApplicationConfig = _root_.pureconfig.loadConfigOrThrow[ApplicationConfig]
}