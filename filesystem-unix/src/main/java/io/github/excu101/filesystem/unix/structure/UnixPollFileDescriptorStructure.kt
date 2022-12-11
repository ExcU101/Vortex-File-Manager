package io.github.excu101.filesystem.unix.structure

import io.github.excu101.filesystem.unix.observer.UnixMasks
import java.io.FileDescriptor

data class UnixPollFileDescriptorStructure(
    val descriptor: FileDescriptor,
    var events: Short = UnixMasks.POLL_IN.toShort(),
    var revents: Short = 0,
)