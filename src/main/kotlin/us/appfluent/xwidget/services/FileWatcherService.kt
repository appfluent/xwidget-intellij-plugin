package us.appfluent.xwidget.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread

@Service(Service.Level.PROJECT)
class FileWatcherService(val project: Project) {
    companion object {
        private val LOG: Logger = Logger.getInstance(FileWatcherService::class.java)
    }

    private val handlers: MutableList<FileChangeHandler> = mutableListOf()

    init {
        VirtualFileManager.getInstance().addAsyncFileListener(
            FileChangeListener(::afterFileChange),
            { handlers.clear() }
        )
    }

    fun startWatching(path: String, callback: (VirtualFile) -> Unit) {
        LOG.debug("START: $path")
        handlers.add(FileChangeHandler(path, callback))
    }

    fun stopWatching(path: String) {
        LOG.debug("STOP: $path")
        handlers.removeIf { it.filePath.endsWith(path) }
    }

    private fun afterFileChange(event: VFileEvent) {
        if (event.file != null) {
            val relevantHandlers = handlers.filter {
                event.path.endsWith(it.filePath)
            }
            for (handler in relevantHandlers) {
                handler.execute(event.file!!)
            }
        }
    }
}

class FileChangeListener(val afterChange: (event: VFileEvent) -> Unit) : AsyncFileListener {
    override fun prepareChange(events: MutableList<out VFileEvent>): AsyncFileListener.ChangeApplier {
        val relevantEvents = events.filter {
            it.file != null
        }
        return object : AsyncFileListener.ChangeApplier {
            @RequiresBackgroundThread
            override fun afterVfsChange() {
                for (event in relevantEvents) {
                    afterChange(event)  // Custom file change handler
                }
            }
        }
    }
}

data class FileChangeHandler(
    val filePath: String,
    private val callback: (VirtualFile) -> Unit
) {
    fun execute(virtualFile: VirtualFile) {
        callback(virtualFile)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileChangeHandler

        if (filePath != other.filePath) return false
        if (callback != other.callback) return false

        return true
    }

    override fun hashCode(): Int {
        var result = filePath.hashCode()
        result = 31 * result + callback.hashCode()
        return result
    }

    override fun toString(): String {
        return "FileChangeHandler(filePath='$filePath', callback=$callback)"
    }
}