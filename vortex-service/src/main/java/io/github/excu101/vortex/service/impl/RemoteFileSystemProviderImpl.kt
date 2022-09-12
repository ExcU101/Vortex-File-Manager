package io.github.excu101.vortex.service.impl

import io.github.excu101.filesystem.fs.FileSystemProvider
import io.github.excu101.vortex.service.api.FileStoreInfo
import io.github.excu101.vortex.service.data.ParcelablePath
import io.github.excu101.vortex.service.remote.RemoteFileSystemProvider

open class RemoteFileSystemProviderImpl(
    private val local: FileSystemProvider,
) : RemoteFileSystemProvider.Stub() {

    override fun isHidden(path: ParcelablePath) {
        local.isHidden(path.value)
    }

    override fun getScheme(): String = local.scheme

    override fun getFileStoreInfo(path: ParcelablePath): FileStoreInfo {
        val store = local.getFileStore(path.value)

        return FileStoreInfo(
            totalSpace = store.totalSpace,
            usableSpace = store.usableSpace,
            unallocatedSpace = store.unallocatedSpace
        )
    }
}