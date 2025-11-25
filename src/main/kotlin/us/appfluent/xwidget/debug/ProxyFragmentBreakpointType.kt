package us.appfluent.xwidget.debug

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.xdebugger.breakpoints.XLineBreakpointTypeBase
import java.util.*


class ProxyFragmentBreakpointType : XLineBreakpointTypeBase(
    "proxy-fragment-breakpoint",
    "Proxy Fragment Breakpoint",
    null
) {
    override fun canPutAt(file: VirtualFile, line: Int, project: Project): Boolean {
        // Allow breakpoints only in files with an XML extension
        return false
    }

    override fun getVisibleStandardPanels(): EnumSet<StandardPanels> {
        return EnumSet.noneOf(StandardPanels::class.java)
    }
}