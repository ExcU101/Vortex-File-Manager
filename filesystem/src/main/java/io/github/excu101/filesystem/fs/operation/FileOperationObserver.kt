package io.github.excu101.filesystem.fs.operation

import io.github.excu101.filesystem.fs.path.Path

interface FileOperationObserver {

    fun onAction(value: Path)

    fun onError(value: Throwable)

    fun onComplete()

}