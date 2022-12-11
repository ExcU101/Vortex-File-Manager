package io.github.excu101.filesystem.unix.structure

data class UnixPathObservableStructureEvent(
    val observable: Int,
    val mask: Int,
    val cookie: Int,
    val name: ByteArray,
)