package us.appfluent.xwidget.debug

import com.intellij.debugger.DebuggerManager
import com.intellij.openapi.project.Project
import com.intellij.xdebugger.DefaultDebugProcessHandler
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import com.intellij.xdebugger.breakpoints.XBreakpointProperties
import com.intellij.xdebugger.breakpoints.XLineBreakpoint
import us.appfluent.xwidget.utils.XWidgetUtils.Companion.postFragmentBreakpoint
import us.appfluent.xwidget.utils.XWidgetUtils.Companion.postFragmentBreakpoints


class FragmentBreakpointListener(val project: Project) : XBreakpointListener<XLineBreakpoint<XBreakpointProperties<*>>> {
    override fun breakpointAdded(breakpoint: XLineBreakpoint<XBreakpointProperties<*>>) {
        if (breakpoint .type is FragmentBreakpointType) {
            val debugProcess = XDebuggerManager.getInstance(project).currentSession?.debugProcess
            if (debugProcess != null) {
                postFragmentBreakpoint(debugProcess, breakpoint)
                println("breakpoint added")
            }
        }
    }

    override fun breakpointRemoved(breakpoint: XLineBreakpoint<XBreakpointProperties<*>>) {
        println("breakpoint removed")
    }

    override fun breakpointChanged(breakpoint: XLineBreakpoint<XBreakpointProperties<*>>) {
        println("breakpoint changed")
    }

    override fun breakpointPresentationUpdated(breakpoint: XLineBreakpoint<XBreakpointProperties<*>>, session: XDebugSession?) {
        println("breakpoint updated")
    }
}