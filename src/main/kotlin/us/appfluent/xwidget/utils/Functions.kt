package us.appfluent.xwidget.utils

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.util.indexing.FileBasedIndex
import com.jetbrains.lang.dart.ide.index.DartClassIndex
import com.jetbrains.lang.dart.psi.*
import relativePath
import us.appfluent.xwidget.ui.showScrollableListDialog


const val XWIDGET_NAMESPACE = "http://www.appfluent.us/xwidget"

fun isXWidgetFragment(psiFile: PsiFile?): Boolean {
    return psiFile is XmlFile &&
           psiFile.document?.rootTag?.namespace == XWIDGET_NAMESPACE
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

fun findXmlTag(psiFile: PsiFile?, editor: Editor?): XmlTag? {
    val offset = editor?.caretModel?.offset
    val element = psiFile?.findElementAt(offset!!)
    return when(element?.parent) {
        is XmlAttributeValue -> element.parent.parent.parent as XmlTag
        is XmlAttribute -> element.parent.parent as XmlTag
        is XmlTag -> element.parent as XmlTag
        else -> null
    }
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
            if (itemIndex != null) psiFiles[itemIndex] else null
        }
    }
}