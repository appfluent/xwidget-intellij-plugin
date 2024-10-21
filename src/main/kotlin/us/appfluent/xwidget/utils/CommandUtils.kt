package us.appfluent.xwidget.utils

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import org.jetbrains.plugins.terminal.TerminalToolWindowFactory
import org.jetbrains.plugins.terminal.TerminalToolWindowManager
import us.appfluent.xwidget.actions.CommandAction.Companion.TAB_NAME

class CommandUtils {
    companion object {
        private val LOG: Logger = Logger.getInstance(CommandUtils::class.java)

        fun runCommandInTerminal(project: Project, command: String, requestFocus: Boolean) {
            val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(TerminalToolWindowFactory.TOOL_WINDOW_ID)
            val contentManager = toolWindow?.contentManager
            val terminal = when (val content = contentManager?.findContent(TAB_NAME)) {
                null -> {
                    val terminalManager = TerminalToolWindowManager.getInstance(project)
                    terminalManager.createShellWidget(project.basePath, TAB_NAME, requestFocus, false)
                }
                else -> {
                    val selectRunnable = Runnable {
                        contentManager.setSelectedContent(content, requestFocus)
                    }
                    when (toolWindow.isActive) {
                        true -> selectRunnable.run()
                        false -> toolWindow.activate(selectRunnable, requestFocus, requestFocus)
                    }
                    TerminalToolWindowManager.findWidgetByContent(content)
                }
            }
            terminal?.sendCommandToExecute(command)
        }
    }
}