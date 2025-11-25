package us.appfluent.xwidget.debug

import com.intellij.ide.highlighter.XmlFileType
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.xdebugger.breakpoints.XLineBreakpointTypeBase
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProviderBase


class FragmentBreakpointType : XLineBreakpointTypeBase(
    "fragment-breakpoint",
    "Fragment Breakpoint",
    FragmentDebuggerEditorsProvider()
) {
    override fun canPutAt(file: VirtualFile, line: Int, project: Project): Boolean {
        // Allow breakpoints only in files with an XML extension
        return file.extension.equals("xml", ignoreCase = true)
    }
}

private class FragmentDebuggerEditorsProvider : XDebuggerEditorsProviderBase() {
    override fun getFileType(): FileType {
        return XmlFileType.INSTANCE
    }

    override fun createExpressionCodeFragment(
        project: Project,
        text: String,
        context: PsiElement?,
        isPhysical: Boolean
    ): PsiFile {
        // Create a temporary XML file that can be used as an expression fragment
        return PsiFileFactory.getInstance(project).createFileFromText(
            "debug_fragment.xml",  // Arbitrary name for the temporary file
            XmlFileType.INSTANCE,  // File type set to XML
            text,                  // Content of the fragment
            0,                     // Whether it should be a physical file
            true                   // Mark it as non-read-only if needed
        )
    }
}