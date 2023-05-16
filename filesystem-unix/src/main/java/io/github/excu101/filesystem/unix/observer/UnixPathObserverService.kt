package io.github.excu101.filesystem.unix.observer

import android.system.OsConstants
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.observer.PathObservableEventType
import io.github.excu101.filesystem.fs.observer.PathObservableKey
import io.github.excu101.filesystem.fs.observer.PathObserverService
import io.github.excu101.filesystem.fs.observer.event.CreateEvent
import io.github.excu101.filesystem.fs.observer.type.*
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.attrs
import io.github.excu101.filesystem.unix.UnixCalls
import io.github.excu101.filesystem.unix.UnixFileSystem
import io.github.excu101.filesystem.unix.calls.UnixFileChannelCalls
import io.github.excu101.filesystem.unix.calls.UnixObserverCalls
import io.github.excu101.filesystem.unix.channel.UnixByteBuffer
import io.github.excu101.filesystem.unix.observer.UnixMasks.IGNORED
import io.github.excu101.filesystem.unix.observer.UnixMasks.POLL_IN
import io.github.excu101.filesystem.unix.observer.UnixMasks.maskWith
import io.github.excu101.filesystem.unix.observer.UnixPollRequest.Cancel
import io.github.excu101.filesystem.unix.path.UnixPath
import io.github.excu101.filesystem.unix.structure.UnixPollFileDescriptorStructure
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import java.io.Closeable
import java.io.FileDescriptor
import kotlin.coroutines.CoroutineContext

internal class UnixPathObserverService(
    private val system: UnixFileSystem,
    private val context: CoroutineContext = IO,
) : PathObserverService, Closeable {

    private val scope = CoroutineScope(context)

    private var closed = false

    private val sockets = arrayOf(FileDescriptor(), FileDescriptor())

    private val pendingKeys = Channel<UnixPathObservableKey>(capacity = BUFFERED)

    private val keys = mutableMapOf<Int, UnixPathObservableKey>()

    private val buffer = ByteArray(size = 8192)

    override val isOpen: Boolean
        get() = !closed

    @OptIn(ObsoleteCoroutinesApi::class)
    private val poller = scope.actor<UnixPollRequest>() {
        UnixCalls.createSocketPair(
            OsConstants.AF_UNIX,
            OsConstants.SOCK_STREAM,
            0,
            sockets[0],
            sockets[1]
        )
        UnixCalls.manipulateDescriptor(sockets[0], OsConstants.F_GETFL)
        val descriptor = UnixObserverCalls.initService(flags = OsConstants.O_NONBLOCK)

        consumeEach { request ->
            if (!isActive) return@actor
            when (request) {
                UnixPollRequest.Run -> {
                    start(descriptor)
                }

                is UnixPollRequest.Register -> {
                    val path = request.path
                    val response = request.response
                    val observable = UnixObserverCalls.addObservable(
                        descriptor = descriptor,
                        path = path.bytes,
                        mask = wrapMask(types = request.types)
                    )

                    val key = UnixPathObservableKey(
                        service = this@UnixPathObserverService,
                        path = path,
                        descriptor = observable
                    )

                    keys[observable] = key
                    response.complete(key)
                }

                is UnixPollRequest.Enqueue -> {
                    val key = request.key
                    pendingKeys.send(key)
                }

                is Cancel -> {
                    val key = request.key
                    UnixObserverCalls.removeObservable(
                        descriptor = descriptor,
                        observable = key.descriptor
                    )
                    key.setNotActual()
                    keys.remove(key.descriptor)
                }

                is UnixPollRequest.Close -> {
                    for (pending in pendingKeys) {
                        UnixObserverCalls.removeObservable(
                            descriptor = descriptor,
                            observable = pending.descriptor
                        )
                        pending.setNotActual()
                    }
                    pendingKeys.close()
                    UnixCalls.close(descriptor = descriptor)
                    closed = true
                }
            }
        }
    }

    private suspend fun start(descriptor: Int) {
        val fds = arrayOf(
            UnixPollFileDescriptorStructure(sockets[0]),
            UnixPollFileDescriptorStructure(sockets[1])
        )
        while (true) {
            UnixObserverCalls.waitEventsForObservable(
                fds,
                -1
            )
            if (fds[0].events.toInt() maskWith POLL_IN) {
                val buffer = UnixByteBuffer(capacity = 1)
                val bytes = UnixFileChannelCalls.read(
                    descriptor = descriptor,
                    address = buffer.address,
                    count = buffer.remaining
                )
                if (bytes > 0) {
                    if (closed) break
                }
            }
            if (fds[1].events.toInt() maskWith POLL_IN) {
                val buffer = UnixByteBuffer(capacity = 8192)
                val bytes = UnixFileChannelCalls.read(
                    descriptor = descriptor,
                    address = buffer.address,
                    count = buffer.remaining
                )
                if (bytes > 0) {
                    val events = UnixObserverCalls.getEventsForObservable(
                        this@UnixPathObserverService.buffer,
                        0,
                        bytes
                    )
                    if (events != null) {
                        for (event in events) {
                            val key = keys[event.observable]

                            if (event.mask maskWith IGNORED) {
                                key?.cancel()
                            }

                            wrapMask(
                                event.mask,
                                system.getPath(event.name.decodeToString()) as UnixPath
                            )?.let { key?.registerEvent(event = it) }
                        }
                    }
                }
            }
        }
    }

    override suspend fun register(
        source: Path,
        vararg types: PathObservableEventType,
    ): PathObservableKey {
        if (source !is UnixPath) throw IllegalArgumentException()
        if (!source.attrs<BasicAttrs>().isDirectory) throw IllegalArgumentException("Source isn't directory ($source)")
        val response = CompletableDeferred<UnixPathObservableKey>()

        poller.send(
            element = UnixPollRequest.Register(
                path = source,
                types = types,
                response = response
            )
        )

        return response.await()
    }

    internal fun enqueueKey(
        key: UnixPathObservableKey,
    ) = scope.launch {
        poller.send(UnixPollRequest.Enqueue(key))
    }

    override suspend fun take(): UnixPathObservableKey? {
        return pendingKeys.receiveCatching().getOrNull()
    }

    override fun close() = runBlocking<Unit> {
        poller.send(UnixPollRequest.Close)
        poller.close()
    }

    internal suspend fun removeObservable(observable: UnixPathObservableKey) {
        poller.send(Cancel(key = observable))
    }

    private fun wrapMask(
        mask: Int,
        context: UnixPath,
    ): PathObservableKey.Event? {
        return when (mask) {
            UnixMasks.CREATE -> {
                CreateEvent(context)
            }

            else -> null
        }
    }

    private fun wrapMask(
        types: Array<out PathObservableEventType>,
    ): Int {
        var mask = 0

        types.forEach { type ->
            when (type) {
                is PathObservableModifyEventType -> {
                    mask = mask or UnixMasks.MODIFY
                }

                is PathObservableDeleteEventType -> {
                    mask = mask or UnixMasks.DELETE
                }

                is PathObservableCreateEventType -> {
                    mask = mask or UnixMasks.CREATE
                }
            }
        }

        return mask
    }
}