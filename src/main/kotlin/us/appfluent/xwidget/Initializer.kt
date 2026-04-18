package us.appfluent.xwidget

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import io.flutter.view.FlutterViewMessages
import org.dartlang.vm.service.VmServiceListener
import org.dartlang.vm.service.element.Event
import us.appfluent.xwidget.XWidgetConstants.Companion.HOT_RELOAD_SINCE_VERSION
import us.appfluent.xwidget.services.XWidgetService


class Initializer : ProjectActivity {
    override suspend fun execute(project: Project) {
        // load preferences and configuration
        val xwidget = project.getService(XWidgetService::class.java)

        // register listeners
        registerListeners(project, xwidget)
    }

    private fun registerListeners(project: Project, xwidget: XWidgetService) {
        val connection = project.messageBus.connect()
        connection.subscribe(FlutterViewMessages.FLUTTER_DEBUG_TOPIC, FlutterViewMessages.FlutterDebugNotifier { event ->
            val sessionId = event.vmService.hashCode().toString()

            if (xwidget.version != null && xwidget.version!! >= HOT_RELOAD_SINCE_VERSION) {
                // start hot reload listeners
                xwidget.startHotReloadFragments(event.vmService, sessionId)
                xwidget.startHotReloadValues(event.vmService, sessionId)
            }

            // add service listener because we need a hook to stop hot reload listeners
            event.vmService.addVmServiceListener(object : VmServiceListener {
                override fun connectionOpened() { }

                override fun received(p0: String?, event: Event?) { }

                override fun connectionClosed() {
                    xwidget.stopHotReloadFragments(sessionId)
                    xwidget.stopHotReloadValues(sessionId)
                }
            })
        })
    }
}