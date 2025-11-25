package us.appfluent.xwidget.debug



import com.intellij.openapi.application.ApplicationManager
import com.intellij.xdebugger.frame.XSuspendContext
import io.flutter.vmService.DartVmServiceBreakpointHandler
import io.flutter.vmService.DartVmServiceDebugProcess
import io.flutter.vmService.DartVmServiceListener
import org.dartlang.vm.service.element.Event
import org.dartlang.vm.service.element.EventKind
import us.appfluent.xwidget.utils.ReflectUtil.getFieldValue


class FragmentVmServiceListener(
    private val myDebugProcess: DartVmServiceDebugProcess,
    private val myBreakpointHandler: DartVmServiceBreakpointHandler
) : DartVmServiceListener(myDebugProcess, myBreakpointHandler) {
    companion object {
        fun from(listener: DartVmServiceListener): FragmentVmServiceListener {
            val debugProcess = getFieldValue<DartVmServiceDebugProcess>(listener, "myDebugProcess")
            val breakpointHandler = getFieldValue<DartVmServiceBreakpointHandler>(listener, "myBreakpointHandler")
            return FragmentVmServiceListener(debugProcess, breakpointHandler)
        }
    }

    override fun received(streamId: String, event: Event) {
        println("streamId=$streamId, " +
                "method=${event.method}, " +
                "kind=${event.kind}, " +
                "extKind=${event.extensionKind}, " +
                "extRPC=${event.extensionRPC}")

        when (event.kind) {
            EventKind.PauseBreakpoint,
            EventKind.PauseException,
            EventKind.PauseInterrupted -> {
                myDebugProcess.isolateSuspended(event.isolate)
                ApplicationManager.getApplication().executeOnPooledThread {
//                    val breakpoints = if (event.kind == EventKind.PauseBreakpoint) event.pauseBreakpoints else null
//                    val exception = if (event.kind == EventKind.PauseException) event.exception else null
                    val suspendContext = FragmentVmServiceSuspendContext(myDebugProcess)
                    myDebugProcess.session.positionReached(suspendContext)
                }
            }
            else -> super.received(streamId, event)
        }

    }
}