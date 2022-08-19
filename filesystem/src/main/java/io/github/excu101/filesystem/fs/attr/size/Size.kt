package io.github.excu101.filesystem.fs.attr.size

import io.github.excu101.filesystem.fs.attr.size.Size.Types.B
import io.github.excu101.filesystem.fs.attr.size.Size.Types.values

class Size(val inputMemory: Long) {

    companion object {
        private infix fun Int.pow(n: Int): Double {
            if (n <= 0) {
                return 1.0
            }
            return this * pow(n - 1)
        }
    }

    private val type: Types
        get() = parseType()

    fun isEmpty(): Boolean = inputMemory == 0L

    override fun toString(): String {
        return round().toString() + type.toString()
    }

    private fun round(): Int {
        return (inputMemory / type.value).toInt()
    }

    private fun parseType(): Types = values().fold(B) { prev, current ->
        if (inputMemory >= current.value) {
            current
        } else {
            prev
        }
    }

    enum class Types(val value: Double) {
        B(value = 2 pow 3),
        KB(value = 2 pow 10),
        MB(value = 2 pow 20),
        GB(value = 2 pow 30),
        TB(value = 2 pow 40),
        PB(value = 2 pow 50),
        EB(value = 2 pow 60),
        ZB(value = 2 pow 70),
        YB(value = 2 pow 80)
    }

}