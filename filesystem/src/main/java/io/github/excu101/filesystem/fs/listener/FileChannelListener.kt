package io.github.excu101.filesystem.fs.listener

interface FileChannelListener {

    fun onWrite(bytes: Int)

    fun onRead(bytes: Int)

    fun onError(error: Throwable)

    fun onClose()

}

inline fun listener(
    crossinline onWrite: (bytes: Int) -> Unit = {},
    crossinline onRead: (bytes: Int) -> Unit = {},
    crossinline onError: (error: Throwable) -> Unit = { throw it },
    crossinline onClose: () -> Unit = {},
): FileChannelListener {
    return object : FileChannelListener {

        override fun onWrite(bytes: Int) {
            onWrite.invoke(bytes)
        }

        override fun onRead(bytes: Int) {
            onRead.invoke(bytes)
        }

        override fun onError(error: Throwable) {
            onError.invoke(error)
        }

        override fun onClose() {
            onClose.invoke()
        }

    }
}