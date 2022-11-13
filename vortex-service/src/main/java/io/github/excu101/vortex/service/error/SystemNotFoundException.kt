package io.github.excu101.vortex.service.error

class SystemNotFoundException : Throwable {
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
    constructor() : super()
}