package io.github.excu101.filesystem.fs.error

fun interface FileSystemErrorHandler {

    fun onFileSystemError(error: Throwable)

}