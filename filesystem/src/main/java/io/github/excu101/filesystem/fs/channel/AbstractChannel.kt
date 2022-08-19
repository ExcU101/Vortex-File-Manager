package io.github.excu101.filesystem.fs.channel

abstract class AbstractChannel : Channel {

    private val lock = Any()
    private var _isOpen = true

    override val isOpen: Boolean
        get() = _isOpen

    protected abstract fun implCloseChannel()

    override fun close() {
        synchronized(lock) {
            if (isOpen) {
                if (!isOpen) return
                _isOpen = false
                implCloseChannel()
            }
        }
    }
}