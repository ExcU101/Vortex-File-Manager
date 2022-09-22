package io.github.excu101.vortex.service.data

import android.os.Parcelable
import io.github.excu101.filesystem.fs.operation.FileOperationObserver
import io.github.excu101.filesystem.fs.path.Path
import kotlinx.parcelize.Parcelize

@Parcelize
open class ParcelableFileOperationObserver : Parcelable, FileOperationObserver {
    override fun onAction(value: Path) {

    }

    override fun onError(value: Throwable) {

    }

    override fun onComplete() {

    }

}