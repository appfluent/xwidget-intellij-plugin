package us.appfluent.xwidget.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import us.appfluent.xwidget.PluginActionPlaces
import us.appfluent.xwidget.utils.CommandUtils


abstract class CommandAction(private val cmd: String) : AnAction() {
    companion object {
        const val TAB_NAME = "XWidget"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val requestFocus = e.place != PluginActionPlaces.BACKGROUND
        CommandUtils.runCommandInTerminal(project, cmd, requestFocus)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        // Choose EDT if interacting with the UI, or BGT for background work
        return ActionUpdateThread.BGT // or ActionUpdateThread.EDT
    }
}