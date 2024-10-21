package us.appfluent.xwidget.utils

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiFile

import us.appfluent.xwidget.ui.showScrollableListDialog
import java.nio.file.Path
import java.nio.file.Paths

fun toAbsolutePath(project: Project, pathStr: String): Path {
    val path = Paths.get(pathStr)
    return if (path.isAbsolute) path else Paths.get("${project.basePath}/$path")
}

fun getOrSelectPsiFile(project: Project, psiFiles: List<PsiFile>): PsiFile? {
    return when(psiFiles.size) {
        0 -> null
        1 -> psiFiles[0]
        else -> {
            val paths = mutableListOf<String>().apply {
                psiFiles.forEach { psiFile -> add(psiFile.relativePath(project)) }
            }
            val itemIndex = showScrollableListDialog(project, "Select Item", paths)
            if (itemIndex != null && itemIndex > -1) psiFiles[itemIndex] else null
        }
    }
}

fun showNotification(project: Project?, message: String, type: NotificationType = NotificationType.INFORMATION) {
    val icon = IconLoader.getIcon("/icons/xwidget-16x16.png", XWidgetUtils::class.java)
    val notification = NotificationGroupManager
        .getInstance()
        .getNotificationGroup("us.appfluent.xwidget.notifications")
        .createNotification("", message, type)
        .setIcon(icon)

    // Notify using the notification manager
    Notifications.Bus.notify(notification, project)
}