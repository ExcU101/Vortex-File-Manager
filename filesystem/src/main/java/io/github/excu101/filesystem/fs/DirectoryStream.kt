package io.github.excu101.filesystem.fs

import io.github.excu101.filesystem.fs.path.Path
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.withIndex
import java.io.Closeable

interface DirectoryStream<P : Path> : Iterable<Path>, Closeable {

    companion object : DirectoryStream<Path> {
        override fun iterator(): Iterator<Path> {
            return iterator {  }
        }

        override fun close() {

        }
    }

    override fun close()

    interface Filter<T> {
        fun accept(value: T): Boolean

        companion object {
            fun <T> acceptAll() = object : Filter<T> {
                override fun accept(value: T): Boolean {
                    return true
                }
            }
        }
    }

}