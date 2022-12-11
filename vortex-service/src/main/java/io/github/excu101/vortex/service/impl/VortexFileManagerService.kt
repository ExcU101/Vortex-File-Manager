package io.github.excu101.vortex.service.impl

import android.content.Context
import android.system.Os
import io.github.excu101.filesystem.unix.UnixFileSystem
import io.github.excu101.filesystem.unix.provider.UnixFileSystemProvider
import io.github.excu101.vortex.VortexFileManagerService
import io.github.excu101.vortex.service.VortexServiceDelegate
import io.github.excu101.vortex.service.VortexServiceDelegateImpl
import io.github.excu101.vortex.service.data.ParcelablePath
import io.github.excu101.vortex.service.data.VortexServiceInfo
import io.github.excu101.vortex.service.error.ProviderNotFoundException
import io.github.excu101.vortex.service.error.SystemNotFoundException
import io.github.excu101.vortex.service.remote.RemoteFileSystem
import io.github.excu101.vortex.service.remote.RemoteFileSystemProvider
import io.github.excu101.vortex.service.remote.VortexFileSystem
import io.github.excu101.vortex.service.remote.VortexFileSystemProvider

class VortexFileManagerService(
    private val context: Context,
) : VortexFileManagerService.Stub() {

    private val _delegate = VortexServiceDelegateImpl(service = this)
    val delegate: VortexServiceDelegate
        get() = _delegate

    private val systems = mutableListOf<RemoteFileSystem>()

    private val provider = UnixFileSystemProvider()
    private val system = UnixFileSystem(provider)

    private var subscribeCount = 0

    private val vortexProvider = VortexFileSystemProvider(provider)
    private val vortexSystem = VortexFileSystem(system, vortexProvider)

    fun getSystem(path: ParcelablePath): RemoteFileSystem {
        return getSystem(path.src.scheme)
    }

    override fun getSystem(scheme: String): RemoteFileSystem {
        return systems.find {
            it.scheme == scheme
        } ?: throw SystemNotFoundException("System wasn't found by scheme($scheme)")
    }

    override fun getProvider(scheme: String): RemoteFileSystemProvider {
        return try {
            getSystem(scheme).provider
        } catch (exception: SystemNotFoundException) {
            throw ProviderNotFoundException("Provider wasn't found by scheme($scheme)", exception)
        }
    }

    override fun getDefaultProvider(): RemoteFileSystemProvider {
        return vortexSystem.provider
    }

    override fun getDefaultSystem(): RemoteFileSystem {
        return vortexSystem
    }

    override fun install(system: RemoteFileSystem) {
        systems.add(system)
    }

    fun subscribe() {
        subscribeCount++
    }

    fun unsubscribe() {
        subscribeCount--
    }

    override fun getInfo(): VortexServiceInfo {
        return VortexServiceInfo(
            processId = Os.getpid(),
            installedFileSystemCount = systems.size,
            connectedCount = subscribeCount
        )
    }

}