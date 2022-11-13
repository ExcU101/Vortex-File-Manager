package io.github.excu101.vortex.service

import io.github.excu101.filesystem.fs.utils.stream
import io.github.excu101.vortex.service.data.ParcelablePath
import io.github.excu101.vortex.service.impl.VortexFileManagerService

class VortexServiceDelegateImpl(
    private val service: VortexFileManagerService,
) : VortexServiceDelegate.Stub() {

    override fun getPath(
        scheme: String,
        first: String,
        other: Array<out String>,
    ): ParcelablePath {
        return service.getSystem(scheme).getPath(first, other)
    }

    override fun getList(from: ParcelablePath?): List<ParcelablePath> {
        if (from == null) return emptyList()
        return from.src.stream.use { stream -> stream.map(::ParcelablePath) }
    }

}