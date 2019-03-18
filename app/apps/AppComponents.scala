package apps

import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import _root_.controllers._
import play.filters.HttpFiltersComponents
import router.Routes

class AppComponents(context: Context)
  extends BuiltInComponentsFromContext(context) with HttpFiltersComponents with AssetsComponents {
  lazy val applicationController = new HomeController(controllerComponents)(executionContext)

  lazy val router = new Routes(httpErrorHandler, applicationController, assets, "/")
}
