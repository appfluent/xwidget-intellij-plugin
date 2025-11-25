package us.appfluent.xwidget.debug

import com.intellij.xdebugger.evaluation.XDebuggerEvaluator.XEvaluationCallback
import com.intellij.xdebugger.frame.XExecutionStack
import com.intellij.xdebugger.frame.XStackFrame
import com.intellij.xdebugger.frame.XValue
import io.flutter.vmService.DartVmServiceDebugProcess
import org.dartlang.vm.service.element.InstanceRef

class FragmentVmServiceExecutionStack(
    private val myDebugProcess: DartVmServiceDebugProcess
): XExecutionStack("hello") {

    private val myIsolateId: String? = null
    private val myTopFrame: XStackFrame? = null
    private val myException: InstanceRef? = null

    override fun getTopFrame(): XStackFrame? {
      return myTopFrame
    }

    override fun computeStackFrames(firstFrameIndex: Int, container: XStackFrameContainer) {
        if (myDebugProcess.isIsolateSuspended(myIsolateId!!)) {
            myDebugProcess.evaluator!!.evaluate("Debugger().stackFrames", object: XEvaluationCallback {
                override fun evaluated(value: XValue) {
                    container.addStackFrames(emptyList<XStackFrame>(), true)
                }
                override fun errorOccurred(error: String) {

                }
            }, null)
        } else {
            container.addStackFrames(emptyList<XStackFrame>(), true)
        }
    }

    private fun beautify(isolateName: String): String {
        val index = isolateName.indexOf(".dart%22%20as%20test;")
        return  if (index > 0) isolateName.substring(0, index + ".dart".length) else isolateName
    }
}