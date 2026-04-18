package us.appfluent.xwidget.services

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.isFile
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread
import us.appfluent.xwidget.utils.FileUtils.Companion.toAbsolutePath
import kotlin.io.path.pathString

@Service(Service.Level.PROJECT)
class FileWatcherService(val project: Project) : Disposable {
    companion object {
        private val LOG: Logger = Logger.getInstance(FileWatcherService::class.java)
    }

    private val handlers: MutableMap<String, FileChangeHandler> = mutableMapOf()

    init {
        VirtualFileManager.getInstance().addAsyncFileListener(
            FileChangeListener(::afterFileChange),
            this
        )
    }

    fun startWatching(id: String, path: String, callback: (VirtualFile) -> Unit) {
        val isDir = (path.endsWith("/"))
        val match = if (isDir) ::matchDirectory else ::matchFile
        val absolutePath = toAbsolutePath(project, path).pathString + if (isDir) "/" else ""
        handlers[id] = (FileChangeHandler(id, absolutePath, match, callback))
        LOG.debug("Started watching '$absolutePath'")
    }

    fun stopWatching(id: String) {
        handlers.remove(id)
        LOG.debug("Stopped watching '$id'")
    }

    override fun dispose() {
        handlers.clear()
    }

    private fun matchFile(path: String, file: VirtualFile): Boolean {
        return file.isFile && file.path == path
    }

    private fun matchDirectory(path: String, file: VirtualFile): Boolean {
        return file.isFile && file.path.startsWith(path)
    }

    private fun afterFileChange(event: VFileEvent) {
        if (event.file != null) {
            val relevantHandlers = handlers.values.filter { it.match(it.path, event.file!!) }
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
    val id: String,
    val path: String,
    val match: (String, VirtualFile) -> Boolean,
    private val callback: (VirtualFile) -> Unit
) {
    fun execute(virtualFile: VirtualFile) {
        callback(virtualFile)
    }

    override fun toString(): String {
        return "FileChangeHandler(id='$id', path='$path', callback=$callback)"
    }
}