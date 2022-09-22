package io.github.excu101.vortex.service.remote

import io.github.excu101.filesystem.fs.FileStore

class VortexFileStore(
    private val local: FileStore,
) : RemoteFileStore.Stub() {

    override fun getTotalSpace(): Long = local.totalSpace

    override fun getUsableSpace(): Long = local.usableSpace

    override fun getUnallocatedSpace(): Long = local.unallocatedSpace

}