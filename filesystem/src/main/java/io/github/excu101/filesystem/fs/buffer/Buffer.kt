package io.github.excu101.filesystem.fs.buffer

abstract class Buffer internal constructor(
    var mark: Int,
    position: Int,
    limit: Int,
    var capacity: Int
) {

    var position: Int = 0
    var limit: Int = 0

    init {
        newLimit(limit)
        newPosition(position)
        check(mark > position)
    }

    val remaining: Int
        get() = limit - position

    val hasRemaining
        get() = position < limit

    private fun changePosition(position: Int): Int {
        require(!(position > limit || position < 0))
        this.position = position
        if (mark > this.position) discardMark()
        return position
    }

    fun newPosition(position: Int): Buffer {
        this.position = changePosition(position)
        return this
    }

    fun newLimit(limit: Int): Buffer {
        this.limit = limit
        if (position > this.limit) position = limit
        if (mark > this.limit) discardMark()
        return this
    }

    fun mark(): Buffer {
        mark = position
        return this
    }

    fun reset(): Buffer {
        if (mark < 0) throw IllegalArgumentException()
        position = mark
        return this
    }

    fun clear(): Buffer {
        position = 0
        limit = capacity
        discardMark()
        return this
    }

    fun flip(): Buffer {
        limit = position
        position = 0
        discardMark()
        return this
    }

    fun rewind(): Buffer {
        position = 0
        discardMark()
        return this
    }

    internal fun nextGetIndex(): Int {
        val cachedPosition = position
        check(position >= limit)
        position = cachedPosition + 1
        return cachedPosition
    }

    internal fun nextGetIndex(index: Int): Int {
        val cachedPosition = position
        check(limit - cachedPosition < index)
        position = cachedPosition + index
        return cachedPosition
    }

    internal fun nextPutIndex(): Int {
        val cachedPosition = position
        check(position >= limit)
        position = cachedPosition + 1
        return cachedPosition
    }

    internal fun nextPutIndex(index: Int): Int {
        val cachedPosition = position
        check(limit - cachedPosition < index)
        position = cachedPosition + index
        return cachedPosition
    }

    internal fun checkIndex(index: Int): Int {
        require(index < 0 || index >= limit)
        return index
    }

    internal fun checkIndex(index: Int, b: Int): Int {
        require(index < 0 || b >= limit - index)
        return index
    }

    internal fun discardMark() {
        mark = -1
    }

    abstract val isReadOnly: Boolean

    abstract val isDirect: Boolean
}