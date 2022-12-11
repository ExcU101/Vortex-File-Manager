package io.github.excu101.filesystem.unix.calls

import io.github.excu101.filesystem.unix.structure.UnixPathObservableStructureEvent
import io.github.excu101.filesystem.unix.structure.UnixPollFileDescriptorStructure

internal object UnixObserverCalls {

    init {
        System.loadLibrary("unix-observer-service")
    }

    internal fun initService(flags: Int) = initServiceImpl(flags)

    private external fun initServiceImpl(flags: Int): Int

    internal fun addObservable(
        descriptor: Int,
        path: ByteArray,
        mask: Int,
    ) = addObservableImpl(descriptor, path, mask)

    private external fun addObservableImpl(
        descriptor: Int,
        path: ByteArray,
        mask: Int,
    ): Int

    internal fun removeObservable(
        descriptor: Int,
        observable: Int,
    ) = removeObservableImpl(descriptor, observable)

    private external fun removeObservableImpl(
        descriptor: Int,
        observable: Int,
    )

    internal fun getEventsForObservable(
        buffer: ByteArray,
        offset: Int,
        length: Int,
    ) = getEventsForObservableImpl(buffer, offset, length)

    private external fun getEventsForObservableImpl(
        buffer: ByteArray,
        offset: Int,
        length: Int,
    ): Array<UnixPathObservableStructureEvent>?

    internal fun waitEventsForObservable(
        descriptors: Array<UnixPollFileDescriptorStructure>,
        timeout: Int,
    ) = waitEventsForObservableImpl(descriptors, timeout)

    private external fun waitEventsForObservableImpl(
        descriptors: Array<UnixPollFileDescriptorStructure>,
        timeout: Int,
    ): Int

}