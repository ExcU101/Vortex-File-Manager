package io.github.excu101.vortex.service.utils

import io.github.excu101.vortex.service.listener.ParcelableFileChannelListener

fun parcelableListener(
    onWrite: (bytes: Int) -> Unit = { },
    onRead: (bytes: Int) -> Unit = { },
    onClose: () -> Unit = { },
): ParcelableFileChannelListener {
    return ParcelableFileChannelListener(onClose = onClose, onRead = onRead, onWrite = onWrite)
}