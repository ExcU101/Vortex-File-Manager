package io.github.excu101.vortex.service.impl

import android.content.Context
import io.github.excu101.filesystem.unix.UnixFileSystem
import io.github.excu101.filesystem.unix.UnixFileSystemProvider
import io.github.excu101.vortex.VortexServiceApi
import io.github.excu101.vortex.service.remote.RemoteFileSystem
import io.github.excu101.vortex.service.remote.RemoteFileSystemProvider
import io.github.excu101.vortex.service.remote.VortexFileSystem
import io.github.excu101.vortex.service.remote.VortexFileSystemProvider

class VortexServiceImpl(private val context: Context) : VortexServiceApi.Stub() {

    private val notifier = Notifier(context = context)

    private val provider = UnixFileSystemProvider()
    private val system = UnixFileSystem(provider)

    private val vortexProvider = VortexFileSystemProvider(provider)
    private val vortexSystem = VortexFileSystem(system, vortexProvider)

    override fun getSystem(): RemoteFileSystem {
        return vortexSystem
    }

    override fun getProvider(): RemoteFileSystemProvider {
        return vortexProvider
    }

    override fun notify(id: Int, message: String) {
        notifier.setMessage(message).notify(id = id)
    }

}