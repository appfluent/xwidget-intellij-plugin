package us.appfluent.xwidget.actions

import com.intellij.openapi.actionSystem.AnActionEvent

import us.appfluent.xwidget.utils.navigateToDartClass
import us.appfluent.xwidget.utils.navigateToFragment


class GenerateAllAction: CommandAction("dart run xwidget:generate")
class GenerateInflatersAction: CommandAction("dart run xwidget:generate --only inflaters")
class GenerateIconsAction: CommandAction("dart run xwidget:generate --only icons")
class GenerateControllersAction: CommandAction("dart run xwidget:generate --only controllers")

class InitNewProjectAction: CommandAction("dart run xwidget:init --new-app")
class InitExistingProjectAction: CommandAction("dart run xwidget:init")

class ViewDocumentationAction : WebsiteAction("https://pub.dev/packages/xwidget")
class ViewIssuesAction : WebsiteAction("https://github.com/appfluent/xwidget/issues")

class GoToControllerAction: XmlNavigationAction("Controller", "name") {
    override fun navigate(e: AnActionEvent) {
        val controllerName = getXmlAttributeValue(e)
        if (controllerName != null) {
            navigateToDartClass(e.project!!, controllerName)
        }
    }
}
class GoToFragmentAction: XmlNavigationAction("fragment", "name") {
    override fun navigate(e: AnActionEvent) {
        val fragmentName = getXmlAttributeValue(e)
        if (fragmentName != null) {
            navigateToFragment(e.project!!, fragmentName)
        }
    }
}