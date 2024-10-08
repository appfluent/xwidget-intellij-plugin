import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

fun PsiFile.relativePath(project: Project): String {
    val startIndex = (project.basePath?.length ?: -1) + 1
    return virtualFile.path.substring(startIndex)
}