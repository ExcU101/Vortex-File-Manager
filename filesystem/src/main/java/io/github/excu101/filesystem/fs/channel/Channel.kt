package io.github.excu101.filesystem.fs.channel

import java.io.Closeable

interface Channel : Closeable {

    val isOpen: Boolean

    override fun close()

}