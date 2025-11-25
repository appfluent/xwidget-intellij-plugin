package us.appfluent.xwidget.debug

import com.intellij.xdebugger.frame.XExecutionStack
import com.intellij.xdebugger.frame.XSuspendContext
import io.flutter.vmService.DartVmServiceDebugProcess
import io.flutter.vmService.frame.DartVmServiceExecutionStack

class FragmentVmServiceSuspendContext(
    private val myDebugProcess: DartVmServiceDebugProcess
): XSuspendContext() {

    private val myActiveExecutionStack: DartVmServiceExecutionStack? = null
    private var myExecutionStacks: List<XExecutionStack>? = null

    override fun getActiveExecutionStack(): XExecutionStack? {
        return myActiveExecutionStack
    }

    override fun computeExecutionStacks(container: XExecutionStackContainer) {
//        val executionStack = FragmentVmServiceExecutionStack()
//        myExecutionStacks = arrayListOf(executionStack)
//        container.addExecutionStack(myExecutionStacks!!, true)
    }
}