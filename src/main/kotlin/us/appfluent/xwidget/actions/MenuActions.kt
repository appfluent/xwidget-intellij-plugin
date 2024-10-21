package us.appfluent.xwidget.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import us.appfluent.xwidget.*
import us.appfluent.xwidget.XWidgetConstants.Companion.CMD_ADD_XWIDGET
import us.appfluent.xwidget.XWidgetConstants.Companion.CMD_GENERATE_ALL
import us.appfluent.xwidget.XWidgetConstants.Companion.CMD_GENERATE_CONTROLLERS
import us.appfluent.xwidget.XWidgetConstants.Companion.CMD_GENERATE_INFLATERS
import us.appfluent.xwidget.XWidgetConstants.Companion.CMD_INIT_EXISTING_APP
import us.appfluent.xwidget.XWidgetConstants.Companion.CMD_INIT_NEW_APP
import us.appfluent.xwidget.XWidgetConstants.Companion.URL_DOCUMENTATION
import us.appfluent.xwidget.XWidgetConstants.Companion.URL_ISSUES
import us.appfluent.xwidget.services.ConfigurationService
import us.appfluent.xwidget.utils.DartUtils
import us.appfluent.xwidget.utils.XWidgetUtils


class GenerateAllAction: CommandAction(CMD_GENERATE_ALL)
class GenerateInflatersAction: CommandAction(CMD_GENERATE_INFLATERS)
class GenerateIconsAction: CommandAction(XWidgetConstants.CMD_GENERATE_ICONS)
class GenerateControllersAction: CommandAction(CMD_GENERATE_CONTROLLERS)

class InitNewProjectAction: CommandAction("$CMD_ADD_XWIDGET && $CMD_INIT_NEW_APP")
class InitExistingProjectAction: CommandAction("$CMD_ADD_XWIDGET && $CMD_INIT_EXISTING_APP")

class ViewDocumentationAction : WebsiteAction(URL_DOCUMENTATION)
class ViewIssuesAction : WebsiteAction(URL_ISSUES)

class GoToControllerAction: XmlNavigationAction("Controller", "name") {
    override fun navigate(e: AnActionEvent) {
        val controllerName = getXmlAttributeValue(e)
        if (controllerName != null) {
            DartUtils.navigateToDartClass(e.project!!, controllerName)
        }
    }
}
class GoToFragmentAction: XmlNavigationAction("fragment", "name") {
    override fun navigate(e: AnActionEvent) {
        val fragmentName = getXmlAttributeValue(e)
        if (fragmentName != null) {
            XWidgetUtils.navigateToFragment(e.project!!, fragmentName)
        }
    }
}

class AutoGenerateAction: ToggleAction() {
    override fun isSelected(e: AnActionEvent): Boolean {
        if (e.project != null) {
            val service = e.project!!.getService(ConfigurationService::class.java)
            return service.autoGenerateEnabled
        }
        return false
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        if (e.project != null) {
            val service = e.project!!.getService(ConfigurationService::class.java)
            service.autoGenerateEnabled = state
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        // Choose EDT if interacting with the UI, or BGT for background work
        return ActionUpdateThread.BGT // or ActionUpdateThread.EDT
    }
}