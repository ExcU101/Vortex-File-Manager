package io.github.excu101.vortex.service.data

import android.os.Parcelable
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.FileOperationObserver
import kotlinx.parcelize.Parcelize

@Parcelize
open class ParcelableFileOperationObserver : Parcelable, FileOperationObserver {
    override fun onAction(action: FileOperation.Action) {

    }

    override fun onError(value: Throwable) {

    }

    override fun onComplete() {

    }

}

inline fun parcelableObserver(
    crossinline onAction: (action: FileOperation.Action) -> Unit = {},
    crossinline onError: (Throwable) -> Unit = {},
    crossinline onComplete: () -> Unit = {},
): ParcelableFileOperationObserver {
    return object : ParcelableFileOperationObserver() {
        override fun onAction(action: FileOperation.Action) {
            onAction.invoke(action)
        }

        override fun onError(value: Throwable) {
            onError.invoke(value)
        }

        override fun onComplete() {
            onComplete.invoke()
        }
    }
}