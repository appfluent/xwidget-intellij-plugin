package us.appfluent.xwidget.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

fun PsiFile.relativePath(project: Project): String {
    val startIndex = (project.basePath?.length ?: -1) + 1
    return virtualFile.path.substring(startIndex)
}

fun List<Any>.isDifferent(other: List<Any>): Boolean {
    return distinct().size != other.distinct().size || !containsAll(other) || !other.containsAll(this)
}