package io.github.excu101.filesystem.unix

import io.github.excu101.filesystem.fs.FileStore
import io.github.excu101.filesystem.fs.FileSystem
import io.github.excu101.filesystem.fs.FileSystemProvider
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.FileSystemHelper
import io.github.excu101.filesystem.unix.path.UnixPath

class UnixFileSystem(
    override val provider: FileSystemProvider,
) : FileSystem(provider = provider) {

    override val separator: Byte
        get() = '/'.code.toByte()

    val rootDirectory = UnixPath(
        _system = this,
        path = byteArrayOf(separator)
    )

    init {
        if (!rootDirectory.isAbsolute) {
            throw AssertionError("Root directory must be absolute")
        }
        if (rootDirectory.nameCount != 0) {
            throw AssertionError("Root directory must contain no names")
        }
    }


    internal val defaultDirectory = UnixPath(
        _system = this,
        path = if (!System.getenv("user.dir").isNullOrEmpty()) {
            System.getenv("user.dir")!!.toByteArray()
        } else {
            byteArrayOf(separator)
        }
    )

    init {
        if (!defaultDirectory.isAbsolute) error("FUCK U")
    }

    override val scheme: String
        get() = "file"

    override val helper: FileSystemHelper = UnixFileSystemHelper()

    override val containsHelper: Boolean = true

    override fun isOpen(): Boolean = true

    override fun isReadOnly(): Boolean = false

    override val stores: Iterable<FileStore>
        get() = UnixFileStore.entries.map { entry ->
            UnixFileStore.from(
                system = this,
                entry = entry
            )
        }

    override fun getPath(first: String, vararg other: String): Path {
        return UnixPath(
            _system = this,
            path = StringBuilder(first).apply {
                other.forEach {
                    append(separator.toInt().toChar().toString())
                    append(it)
                }
            }.toString().encodeToByteArray()
        )
    }

}