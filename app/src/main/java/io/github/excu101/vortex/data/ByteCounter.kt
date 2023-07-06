package io.github.excu101.vortex.data

import io.github.excu101.filesystem.fs.attr.size.Size

class ByteCounter(
    initial: Long = 0L,
    private val convertor: (Long) -> Size,
) {
    var current: Long = initial
    val isEmpty: Boolean
        get() = current == 0L

    var isChanged: Boolean = true

    val size: Size = convertor(current)
        get() = if (isChanged) convertor(current) else field
}