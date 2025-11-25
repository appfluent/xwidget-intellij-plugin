package us.appfluent.xwidget.utils

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager

import java.nio.file.Path
import java.nio.file.Paths


class FileUtils {
    companion object {
        fun toAbsolutePath(project: Project, pathStr: String): Path {
            val path = Paths.get(pathStr)
            return if (path.isAbsolute) path else Paths.get("${project.basePath}/$path")
        }

        fun findVirtualFile(project: Project, path: String): VirtualFile? {
            return VirtualFileManager.getInstance().findFileByNioPath(toAbsolutePath(project, path))
        }

        fun findLineNumber(file: VirtualFile, searchText: String): Int {
            val document = FileDocumentManager.getInstance().getDocument(file)
            if (document != null) {
                val offset = document.text.indexOf(searchText)
                if (offset >= 0) {
                    return document.getLineNumber(offset)
                }
            }
            return -1
        }

        fun openFileInEditor(project: Project, filePath: String, lineNumber: Int) {
            // Locate the file in the project
            val virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://$filePath")
            if (virtualFile != null) {
                // Open the file in the editor and navigate to the specified line
                FileEditorManager.getInstance(project).openTextEditor(
                    OpenFileDescriptor(project, virtualFile, lineNumber - 1, 0), true
                )
            }
        }
    }
}

class FileInfo(filePath: String) {
    val path: String
    val name: String
    val extension: String
    val fullPath: String
    init {
        val fullPath = Paths.get(filePath)
        this.fullPath = fullPath.toString()
        this.path = fullPath.parent?.toString() ?: ""
        this.name = fullPath.fileName?.toString() ?: ""
        this.extension = when (val extIndex = this.name.lastIndexOf(".")) {
            -1, 0 -> ""
            else -> name.substring(extIndex + 1)
        }
    }

    fun hasPath(): Boolean {
        return path.isNotEmpty()
    }

    fun hasName(): Boolean {
        return name.isNotEmpty()
    }

    fun hasExtension(): Boolean {
        return extension.isNotEmpty()
    }

    override fun toString(): String {
        return "FileInfo(path='$path', name='$name', extension='$extension', fullPath='$fullPath')"
    }
}
