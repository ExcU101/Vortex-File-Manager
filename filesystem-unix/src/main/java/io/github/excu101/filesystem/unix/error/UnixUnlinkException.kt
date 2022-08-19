package io.github.excu101.filesystem.unix.error

class UnixUnlinkException  : Throwable{

    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor() : super()
}