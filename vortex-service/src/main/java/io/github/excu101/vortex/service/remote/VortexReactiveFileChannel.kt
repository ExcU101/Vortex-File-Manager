package io.github.excu101.vortex.service.remote

import io.github.excu101.filesystem.fs.channel.ReactiveFileChannel
import io.github.excu101.vortex.service.listener.ParcelableFileChannelListener

class VortexReactiveFileChannel(
    private val local: ReactiveFileChannel,
) : RemoteReactiveFileChannel.Stub() {

    override fun write(): RemoteReactiveFileChannel {
        local.write()
        return this
    }

    override fun read(): RemoteReactiveFileChannel {
        local.read()
        return this
    }

    override fun close() {
        local.close()
    }

    override fun addListener(listener: ParcelableFileChannelListener): RemoteReactiveFileChannel {
        local.addListener(listener)
        return this
    }

    override fun removeListener(listener: ParcelableFileChannelListener): RemoteReactiveFileChannel {
        local.removeListener(listener)
        return this
    }


}