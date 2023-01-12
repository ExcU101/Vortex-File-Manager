package io.github.excu101.vortex.service.impl

import android.system.Os
import io.github.excu101.vortex.VortexFileManagerService
import io.github.excu101.vortex.service.data.ParcelablePath
import io.github.excu101.vortex.service.data.VortexServiceInfo
import io.github.excu101.vortex.service.error.ProviderNotFoundException
import io.github.excu101.vortex.service.error.SystemNotFoundException
import io.github.excu101.vortex.service.remote.RemoteFileSystem
import io.github.excu101.vortex.service.remote.RemoteFileSystemProvider
import io.github.excu101.vortex.service.remote.VortexFileSystem

class VortexFileManagerService(
//    private val center: NotificationCenter,
) : VortexFileManagerService.Stub() {

    private val systems = mutableListOf<RemoteFileSystem>()

    private var subscribeCount = 0

    private var vortexSystem: VortexFileSystem? = null

    fun getSystem(path: ParcelablePath): RemoteFileSystem {
        return getSystem(path.src.scheme)
    }

    override fun getSystem(scheme: String): RemoteFileSystem {
        if (scheme == defaultSystem.scheme) return defaultSystem

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

    override fun getDefaultProvider(): RemoteFileSystemProvider = defaultSystem.provider

    override fun getDefaultSystem(): RemoteFileSystem {
        requireNotNull(vortexSystem)
        return vortexSystem as VortexFileSystem
    }

    override fun install(system: RemoteFileSystem) {
        systems.add(system)
    }

    override fun installDefault(system: RemoteFileSystem?) {
        require(vortexSystem == null) { "Default service file system already set" }

        vortexSystem = system as VortexFileSystem
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