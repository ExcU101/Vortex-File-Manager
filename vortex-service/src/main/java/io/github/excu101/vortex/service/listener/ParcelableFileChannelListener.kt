package io.github.excu101.vortex.service.listener

import android.os.Parcelable
import io.github.excu101.filesystem.fs.listener.FileChannelListener
import kotlinx.parcelize.Parcelize

@Parcelize
class ParcelableFileChannelListener(
    private val onClose: () -> Unit,
    private val onWrite: (bytes: Int) -> Unit,
    private val onRead: (bytes: Int) -> Unit,
) : FileChannelListener, Parcelable {
    override fun onWrite(bytes: Int) {
        onWrite.invoke(bytes)
    }

    override fun onRead(bytes: Int) {
        onRead.invoke(bytes)
    }

    override fun onError(error: Throwable) {

    }

    override fun onClose() {
        onClose.invoke()
    }
}