package us.appfluent.xwidget.actions

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

abstract class WebsiteAction(private val url: String) : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        BrowserUtil.browse(url)
    }
}

