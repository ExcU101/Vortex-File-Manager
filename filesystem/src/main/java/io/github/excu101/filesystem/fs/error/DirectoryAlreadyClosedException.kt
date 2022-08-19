package io.github.excu101.filesystem.fs.error

class DirectoryAlreadyClosedException : Throwable {

    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor() : super()
}