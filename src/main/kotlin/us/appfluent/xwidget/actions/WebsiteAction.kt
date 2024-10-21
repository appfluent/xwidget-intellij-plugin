package us.appfluent.xwidget.actions

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

abstract class WebsiteAction(private val url: String) : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        BrowserUtil.browse(url)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        // Choose EDT if interacting with the UI, or BGT for background work
        return ActionUpdateThread.BGT // or ActionUpdateThread.EDT
    }
}

