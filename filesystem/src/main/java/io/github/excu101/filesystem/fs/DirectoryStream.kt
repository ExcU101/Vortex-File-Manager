package io.github.excu101.filesystem.fs

import io.github.excu101.filesystem.fs.path.Path
import java.io.Closeable

interface DirectoryStream<P : Path> : Iterable<Path>, Closeable {

    companion object : DirectoryStream<Path> {
        override fun iterator(): Iterator<Path> {
            return iterator { }
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

            fun <T : Path> excludeCurrentAndPrevieousDirs() = object : Filter<T> {
                override fun accept(value: T): Boolean {
                    val bytes = value.bytes
                    if (bytes.contentEquals(".".toByteArray())) return false
                    if (bytes.contentEquals("..".toByteArray())) return false

                    return true
                }
            }
        }
    }

}