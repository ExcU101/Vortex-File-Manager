package io.github.excu101.vortex.service.remote

import io.github.excu101.filesystem.fs.FileSystem
import io.github.excu101.vortex.service.data.ParcelablePath


class VortexFileSystem @JvmOverloads constructor(
    private val local: FileSystem,
    private val provider: VortexFileSystemProvider = VortexFileSystemProvider(local.provider),
) : RemoteFileSystem.Stub() {

    override fun getProvider(): RemoteFileSystemProvider = provider

    override fun isOpen(): Boolean = local.isOpen()

    override fun isReadOnly(): Boolean = local.isReadOnly()

    override fun getSeparator(): Byte = local.separator

    override fun getScheme(): String = local.scheme

    override fun getPath(segment: String, other: Array<out String>): ParcelablePath {
        return ParcelablePath(local.getPath(segment, *other))
    }
}