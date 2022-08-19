package io.github.excu101.filesystem.fs.buffer

class HeapByteBuffer : ByteBuffer {

    constructor(capacity: Int, limit: Int) : super(
        mark = -1,
        position = 0,
        limit = limit,
        capacity = capacity,
        offset = 0
    ) {
       this.heapBuffer = ByteArray(capacity)
    }

    constructor(heap: ByteArray, offset: Int, length: Int) : super(
        mark = -1,
        position = offset,
        limit = offset + length,
        capacity = heap.size,
        offset = 0
    ) {
        this.heapBuffer = heap
    }

    protected constructor(
        heap: ByteArray,
        mark: Int,
        position: Int,
        limit: Int,
        capacity: Int,
        offset: Int,
    ) : super(
        mark = mark,
        position = position,
        limit = limit,
        capacity = capacity,
        offset = offset,
    ) {
        this.heapBuffer = heap
    }

    internal val heapBuffer: ByteArray

    private fun indexWithOffset(index: Int): Int {
        return index + offset
    }

    internal fun unsafeGet(index: Int): Byte {
        return heapBuffer[index]
    }

    internal fun unsafePut(index: Int, value: Byte) {
        heapBuffer[index] = value
    }

    fun get(): Byte = get(nextGetIndex())

    override operator fun get(index: Int): Byte {
        return heapBuffer[indexWithOffset(checkIndex(index))]
    }

    override fun put(value: Byte): ByteBuffer = put(index = nextPutIndex(), value = value)

    override fun put(index: Int, value: Byte): ByteBuffer {
        heapBuffer[indexWithOffset(checkIndex(index))] = value
        return this
    }

    override fun getChar(): Char {
        return getChar(nextGetIndex(index = 2))
    }

    override fun getChar(index: Int): Char {
        return unsafeGetChar(index = indexWithOffset(index = checkIndex(index = index, b = 2)))
    }

    override fun putChar(symbol: Char): ByteBuffer {
        val index = indexWithOffset(nextPutIndex(index = 2))
        unsafePutChar(index, symbol)
        return this
    }

    override fun putChar(index: Int, symbol: Char): ByteBuffer {
        val checkedIndex = indexWithOffset(checkIndex(index = index, b = 2))
        unsafePutChar(checkedIndex, symbol)
        return this
    }

    override fun set(index: Int, value: Byte): ByteBuffer = put(index = index, value = value)

    override fun set(index: Int, value: Char): ByteBuffer = putChar(index = index, symbol = value)

    override fun slice(): ByteBuffer {
        val cachedPosition = position
        val cachedLimit = limit
        val rem = if (cachedPosition < cachedLimit) cachedLimit - cachedPosition else 0

        return HeapByteBuffer(
            heap = heapBuffer,
            mark = -1,
            position = 0,
            limit = rem,
            capacity = rem,
            offset = cachedPosition + offset
        )
    }

    override fun duplicate(): ByteBuffer = HeapByteBuffer(
        heap = heapBuffer,
        mark = mark,
        position = position,
        limit = limit,
        capacity = capacity,
        offset = offset
    )

    override fun compact(): ByteBuffer {
        val cachedPosition = position
        val rem = limit - cachedPosition
        System.arraycopy(
            heapBuffer,
            indexWithOffset(index = cachedPosition),
            heapBuffer,
            indexWithOffset(index = 0),
            rem
        )
        newPosition(position = rem)
        newLimit(capacity)
        discardMark()
        return this
    }

    override fun asReadOnly(): ByteBuffer = HeapByteBuffer(
        heap = heapBuffer,
        mark = mark,
        position = position,
        limit = limit,
        capacity = capacity,
        offset = offset
    )

    override val isReadOnly: Boolean = false

    override val isDirect: Boolean = false
}