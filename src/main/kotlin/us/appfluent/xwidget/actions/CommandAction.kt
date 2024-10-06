package us.appfluent.xwidget.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import org.jetbrains.plugins.terminal.ShellTerminalWidget
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
        val terminalManager = TerminalToolWindowManager.getInstance(project)
        val window = ToolWindowManager.getInstance(project).getToolWindow(TerminalToolWindowFactory.TOOL_WINDOW_ID)
        val contentManager = window?.contentManager
        val widget = when (val content = contentManager?.findContent(TAB_NAME)) {
            null -> terminalManager.createLocalShellWidget(project.basePath, TAB_NAME)
            else -> {
                contentManager.setSelectedContent(content)
                TerminalToolWindowManager.getWidgetByContent(content) as ShellTerminalWidget
            }
        }
        widget.executeCommand(cmd)
    }
}