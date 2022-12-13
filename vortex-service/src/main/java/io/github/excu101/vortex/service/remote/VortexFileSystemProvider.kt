package io.github.excu101.vortex.service.remote

import io.github.excu101.filesystem.fs.provider.FileSystemProvider
import io.github.excu101.vortex.service.data.ParcelableFileOperation
import io.github.excu101.vortex.service.data.ParcelableFileOperationObserver
import io.github.excu101.vortex.service.data.ParcelablePath

class VortexFileSystemProvider(
    private val local: FileSystemProvider,
) : RemoteFileSystemProvider.Stub() {

    override fun runOperation(
        operation: ParcelableFileOperation,
        observers: Array<out ParcelableFileOperationObserver>,
    ) {
        local.runOperation(
            operation = operation,
            observers = observers.toList()
        )
    }

    override fun isHidden(path: ParcelablePath): Boolean {
        return local.isHidden(path.src)
    }

    override fun getScheme(): String = local.scheme

    override fun getFileStore(path: ParcelablePath): RemoteFileStore {
        return VortexFileStore(local.getFileStore(path = path.src))
    }

    override fun newReactiveFileChannel(
        path: ParcelablePath,
        flags: Int,
        mode: Int,
    ): RemoteReactiveFileChannel {
        return VortexReactiveFileChannel(
            local = local.newReactiveFileChannel(
                path = path.src,
                flags = flags,
                mode = mode
            )
        )
    }

}