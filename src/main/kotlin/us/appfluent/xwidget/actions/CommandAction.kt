package us.appfluent.xwidget.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import org.jetbrains.plugins.terminal.TerminalToolWindowFactory
import org.jetbrains.plugins.terminal.TerminalToolWindowManager


abstract class CommandAction(private val cmd: String) : AnAction() {
    companion object {
        const val TAB_NAME = "XWidget"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        runCommand(project, cmd)
    }

    private fun runCommand(project: Project, cmd: String) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(TerminalToolWindowFactory.TOOL_WINDOW_ID)
        val contentManager = toolWindow?.contentManager
        val terminal = when (val content = contentManager?.findContent(TAB_NAME)) {
            null -> {
                val terminalManager = TerminalToolWindowManager.getInstance(project)
                terminalManager.createShellWidget(project.basePath, TAB_NAME, true, false)
            }
            else -> {
                val selectRunnable = Runnable {
                    contentManager.setSelectedContent(content, true)
                }
                when (toolWindow.isActive) {
                    true -> selectRunnable.run()
                    false -> toolWindow.activate(selectRunnable, true, true)
                }
                TerminalToolWindowManager.findWidgetByContent(content)
            }
        }
        terminal?.sendCommandToExecute(cmd)
    }
}