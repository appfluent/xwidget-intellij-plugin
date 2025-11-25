package us.appfluent.xwidget.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import us.appfluent.xwidget.PluginActionPlaces
import us.appfluent.xwidget.services.XWidgetService
import us.appfluent.xwidget.utils.CommandUtils
import us.appfluent.xwidget.utils.Version


abstract class CommandAction : AnAction() {
    companion object {
        const val TAB_NAME = "XWidget"
    }

    abstract fun buildCommand(e: AnActionEvent, version: Version?): String

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val requestFocus = e.place != PluginActionPlaces.BACKGROUND
        val service = project.getService(XWidgetService::class.java)
        val cmd = buildCommand(e, service.version)
        CommandUtils.runCommandInTerminal(project, cmd, requestFocus)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        // Choose EDT if interacting with the UI, or BGT for background work
        return ActionUpdateThread.BGT // or ActionUpdateThread.EDT
    }
}