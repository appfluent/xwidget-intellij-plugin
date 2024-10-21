package us.appfluent.xwidget.utils

import java.nio.file.Paths

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