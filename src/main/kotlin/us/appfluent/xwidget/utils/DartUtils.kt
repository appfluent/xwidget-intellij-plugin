package us.appfluent.xwidget.utils

import com.intellij.notification.NotificationType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.indexing.FileBasedIndex
import com.jetbrains.lang.dart.ide.index.DartClassIndex
import com.jetbrains.lang.dart.psi.DartClass
import com.jetbrains.lang.dart.psi.DartFile
import com.jetbrains.lang.dart.psi.DartVarDeclarationList

class DartUtils {
    companion object {
        private val LOG: Logger = Logger.getInstance(DartUtils::class.java)

        fun getDartFile(project: Project, virtualFile: VirtualFile): DartFile? {
            val psiFile = PsiManager.getInstance(project).findFile(virtualFile)
            if (psiFile != null) {
                if (psiFile is DartFile) {
                    return psiFile
                }
                val message = "${psiFile.name} is not a Dart file."
                showNotification(project, message, NotificationType.WARNING)
                LOG.warn(message)
            }
            return null;
        }

        fun navigateToDartClass(project: Project, className: String, psiFile: PsiFile) {
            val dartClass = PsiTreeUtil
                .findChildrenOfType(psiFile, DartClass::class.java)
                .firstOrNull { it.name == className }
            if (dartClass != null) {
                dartClass.navigate(true)
            } else {
                val title = "Dart Class Not Found"
                val message = "Cannot find Dart class '$className' in file ${psiFile.name}. Please check the spelling."
                Messages.showWarningDialog(project, message, title)
            }
        }

        fun navigateToDartClass(project: Project, className: String) {

            // find all dart files that have this classname defined
            val psiFiles = mutableListOf<PsiFile>()
            val psiManager = PsiManager.getInstance(project)
            val scope = GlobalSearchScope.projectScope(project)
            val classKeys = FileBasedIndex.getInstance().getAllKeys(DartClassIndex.DART_CLASS_INDEX, project)
            for (classKey in classKeys) {
                // Get the files containing the class
                if (classKey == className) {
                    val virtualFiles = FileBasedIndex
                        .getInstance()
                        .getContainingFiles(DartClassIndex.DART_CLASS_INDEX, classKey, scope)
                    for (virtualFile in virtualFiles) {
                        val psiFile = psiManager.findFile(virtualFile)
                        if (psiFile is DartFile) {
                            psiFiles.add(psiFile)
                        }
                    }
                }
            }

            // if files found, then select (if more than 1) and navigate to the class
            if (psiFiles.isNotEmpty()) {
                val selected = getOrSelectPsiFile(project, psiFiles)
                if (selected != null) navigateToDartClass(project, className, selected)
            } else {
                val title = "Dart Class Not Found"
                val message = "Cannot find Dart class '$className'. Please check the spelling."
                Messages.showWarningDialog(project, message, title)
            }
        }
        fun getDartListItems(varList: DartVarDeclarationList): List<PsiElement> {
            val items: MutableList<PsiElement> = mutableListOf()
            val varExpression = varList.varInit?.expression
            if (varExpression != null) {
                for (item in varExpression.children) {
                    items.add(item)
                }
            }
            return items;
        }
    }
}