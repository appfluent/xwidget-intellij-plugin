package us.appfluent.xwidget

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import us.appfluent.xwidget.services.XWidgetService

/// *************************************************************
/// *************************************************************
///
/// WIP: Keep commented code - working on fragment breakpoints
///
/// *************************************************************
/// *************************************************************


//import com.intellij.xdebugger.XDebuggerManager
//import com.intellij.xdebugger.XDebuggerUtil
//import com.intellij.xdebugger.breakpoints.XBreakpointListener
//import com.jetbrains.lang.dart.ide.runner.DartLineBreakpointType
//import io.flutter.view.FlutterViewMessages
//import io.flutter.view.FlutterViewMessages.FlutterDebugNotifier
//import io.flutter.vmService.DartVmServiceDebugProcess
//import io.flutter.vmService.DartVmServiceListener
//import org.dartlang.vm.service.VmService
//import org.dartlang.vm.service.VmServiceListener
//import us.appfluent.xwidget.XWidgetConstants.Companion.ELEMENT_START_BREAKPOINT
//import us.appfluent.xwidget.XWidgetConstants.Companion.XWIDGET_SRC_PATH
//import us.appfluent.xwidget.debug.FragmentBreakpointListener
//import us.appfluent.xwidget.debug.FragmentVmServiceListener

//import us.appfluent.xwidget.utils.DartUtils.Companion.findDartFile
//import us.appfluent.xwidget.utils.FileUtils.Companion.findLineNumber
//import us.appfluent.xwidget.utils.ReflectUtil.getFieldValue


class Initializer : ProjectActivity {
    override suspend fun execute(project: Project) {
        // load preferences and configuration
        project.getService(XWidgetService::class.java)

        // register listeners
    }

//    private fun registerListeners(project: Project) {
//        val connection = project.messageBus.connect()
//        connection.subscribe(XBreakpointListener.TOPIC, FragmentBreakpointListener(project))
//        connection.subscribe(FlutterViewMessages.FLUTTER_DEBUG_TOPIC, FlutterDebugNotifier { event ->
//            replaceDartVmServiceListener(event.vmService)
//            addFragmentVmServiceListener(event.vmService)
//            addProxyBreakpoints(event.app.flutterDebugProcess)
//        })
//    }
//
//    private fun addFragmentVmServiceListener(vmService: VmService) {
//        vmService.addVmServiceListener(FragmentVmServiceListener(event.app))
//    }
//
//    private fun replaceDartVmServiceListener(vmService: VmService) {
//        val listeners = getFieldValue<ArrayList<VmServiceListener>>(vmService, "vmListeners")
//        for ((index, listener) in listeners.withIndex()) {
//            if (listener is DartVmServiceListener) {
//                listeners[index] = FragmentVmServiceListener.from(listener)
//                break
//            }
//        }
//    }
//
//    private fun addProxyBreakpoints(debugProcess: DartVmServiceDebugProcess) {
//        val breakpointManager = XDebuggerManager.getInstance(debugProcess.session.project).breakpointManager
//        val breakpointType = XDebuggerUtil.getInstance().findBreakpointType(DartLineBreakpointType::class.java)
//        if (breakpointType != null) {
//            val file = findDartFile(debugProcess.session.project, XWIDGET_SRC_PATH)
//            val line = findLineNumber(file!!, ELEMENT_START_BREAKPOINT)
//            val breakpoint = breakpointManager.addLineBreakpoint(breakpointType, file.url, line, null)
//            println("Breakpoint created: $breakpoint")
//        } else {
//            println("Breakpoint type not found")
//        }
//
//    }
}