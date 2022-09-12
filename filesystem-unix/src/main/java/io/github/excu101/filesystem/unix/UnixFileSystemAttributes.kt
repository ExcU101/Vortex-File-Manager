package io.github.excu101.filesystem.unix

import io.github.excu101.filesystem.FileSystemAttributes

class UnixFileSystemAttributes : FileSystemAttributes() {

    override val name: String
        get() = "Unix"

    override val scheme: String
        get() = "file"

}