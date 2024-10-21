package us.appfluent.xwidget.utils

import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlFile
import com.jetbrains.lang.dart.psi.DartImportStatement
import com.jetbrains.lang.dart.psi.DartVarDeclarationList
import us.appfluent.xwidget.XWidgetConstants.Companion.NAMESPACE
import us.appfluent.xwidget.services.IconSpec
import us.appfluent.xwidget.services.InflaterSpec

class XWidgetUtils {
    companion object {
        private val LOG: Logger = Logger.getInstance(XWidgetUtils::class.java)

        fun isFragment(psiFile: PsiFile?): Boolean {
            return psiFile is XmlFile && psiFile.document?.rootTag?.namespace == NAMESPACE
        }

        fun navigateToFragment(project: Project, fragmentName: String) {

            // parse fragment name into file parts i.e. path, name, extension
            val fileInfo = FileInfo(when(val queryIndex = fragmentName.indexOf("?")) {
                -1 -> fragmentName.substring(0)
                else -> fragmentName.substring(0, queryIndex)
            })

            // construct a list of potential fragment files
            val fragmentFiles = mutableListOf<FileInfo>().apply {
                if (fileInfo.hasExtension()) {
                    add(fileInfo)
                } else {
                    add(FileInfo(fileInfo.fullPath + ".xml"))
                    add(FileInfo(fileInfo.fullPath + "/index.xml"))
                }
            }

            // find all potential fragment files
            val psiFiles = mutableListOf<PsiFile>()
            val psiManager = PsiManager.getInstance(project)
            val scope = GlobalSearchScope.projectScope(project)
            for (fragmentFile in fragmentFiles) {
                val virtualFiles = FilenameIndex.getVirtualFilesByName(fragmentFile.name, scope)
                for (virtualFile in virtualFiles) {
                    val psiFile = psiManager.findFile(virtualFile)
                    if (psiFile?.virtualFile?.path?.endsWith(fragmentFile.fullPath) == true) {
                        psiFiles.add(psiFile)
                    }
                }
            }

            // if files found, then select (if more than 1) and navigate to the file
            if (psiFiles.isNotEmpty()) {
                val selected = getOrSelectPsiFile(project, psiFiles)
                selected?.navigate(true)
            } else {
                val title = "Fragment Not Found"
                val message = "Cannot find Fragment with name '${fileInfo.fullPath}'. Please check the spelling."
                Messages.showWarningDialog(project, message, title)
            }
        }

        fun getInflaterSpec(project: Project, virtualFile: VirtualFile): InflaterSpec {
            val imports: MutableList<PsiElement> = mutableListOf()
            var inflaters: List<PsiElement>? = null
            ReadAction.run<Throwable> {
                DartUtils.getDartFile(project, virtualFile)?.children?.forEach { child ->
                    if (child is DartImportStatement) {
                        imports.add(child)
                    } else if (child is DartVarDeclarationList && child.varAccessDeclaration.name == "inflaters") {
                        inflaters = DartUtils.getDartListItems(child)
                    }
                }
            }
            return InflaterSpec(imports, inflaters)
        }

        fun getIconSpec(project: Project, virtualFile: VirtualFile): IconSpec {
            val imports: MutableList<PsiElement> = mutableListOf()
            var icons: List<PsiElement>? = null
            var iconSet: List<PsiElement>? = null
            ReadAction.run<Throwable> {
                DartUtils.getDartFile(project, virtualFile)?.children?.forEach { child ->
                    if (child is DartImportStatement) {
                        imports.add(child)
                    } else if (child is DartVarDeclarationList && child.varAccessDeclaration.name == "icons") {
                        icons = DartUtils.getDartListItems(child)
                    } else if (child is DartVarDeclarationList && child.varAccessDeclaration.name == "iconSet") {
                        iconSet = DartUtils.getDartListItems(child)
                    }
                }
            }
            return IconSpec(imports, icons, iconSet)
        }
    }
}

