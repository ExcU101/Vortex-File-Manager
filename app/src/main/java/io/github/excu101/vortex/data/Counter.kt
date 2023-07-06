package io.github.excu101.vortex.data

class Counter(initial: Int) {

    var value: Int = initial
        private set

    var isBiggerThanLast = false
        private set

    val isZero: Boolean
        get() = value == 0

    fun replace(value: Int): Counter {
        if (value == this.value) return this
        isBiggerThanLast = this.value > value
        this.value = value
        return this
    }

    operator fun inc(): Counter {
        value++
        isBiggerThanLast = true
        return this
    }

    operator fun plus(value: Int): Counter {
        this.value += value
        isBiggerThanLast = true
        return this
    }

    operator fun minus(value: Int): Counter {
        this.value += value
        isBiggerThanLast = false
        return this
    }

    operator fun dec(): Counter {
        value--
        isBiggerThanLast = false
        return this
    }

    override fun toString(): String {
        return value.toString()
    }

}