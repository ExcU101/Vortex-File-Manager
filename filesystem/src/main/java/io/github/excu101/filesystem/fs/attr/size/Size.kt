package io.github.excu101.filesystem.fs.attr.size

import kotlinx.coroutines.*
import java.text.DecimalFormat

class Size(val memory: Long) {

    companion object {
        const val simplePattern = "#"

        private infix fun Int.pow(n: Int): Long {
            if (n <= 0) {
                return 1
            }

            return this * pow(n - 1)
        }
    }

    enum class BinaryPrefixes(val value: Long) {
        B(value = 2 pow 3),
        KiB(value = 2 pow 10),
        MiB(value = 2 pow 20),
        GiB(value = 2 pow 30),
        TiB(value = 2 pow 40),
        PiB(value = 2 pow 50),
        EiB(value = 2 pow 60),
        ZiB(value = 2 pow 70),
        YiB(value = 2 pow 80)
    }

    enum class SiPrefixes(val value: Long) {
        B(value = 1L),
        KB(value = 1000L),
        MB(value = 1000000L),
        GB(value = 1000000000L),
        TB(value = 1000000000000L),
        PB(value = 1000000000000000L),
        EB(value = 1000000000000000000L),
        ZB(value = EB.value * 1000L),
        YB(value = ZB.value * 1000L),
    }

    var pattern = simplePattern

    var name: String = "B"

    operator fun plus(other: Size): Size {
        return Size(memory = memory + other.memory)
    }

    operator fun minus(other: Size): Size {
        if (other.memory > memory) {
            return Size(memory = other.memory - memory)
        }
        return Size(memory = memory - other.memory)
    }

    fun isEmpty(): Boolean = memory > 0L

    fun toBinaryType(): String {
        if (memory < 0L) throw IllegalArgumentException("Invalid file size: $memory")

        val type = BinaryPrefixes.values().fold(BinaryPrefixes.B) { prev, current ->
            if (memory > current.value) {
                current
            } else {
                prev
            }
        }

        return formatSize(type.value, type.name)
    }

    fun toSiType(): String {
        if (memory < 0L) throw IllegalArgumentException("Invalid file size: $memory")

        val type = SiPrefixes.values().fold(SiPrefixes.B) { prev, current ->
            if (memory > current.value) {
                current
            } else {
                prev
            }
        }

        return formatSize(type.value, type.name)
    }

    override fun toString(): String {
        return toSiType()
    }

    private fun formatSize(divider: Long, unitName: String): String {
        name = unitName
        return DecimalFormat(pattern)
            .format(memory / divider)
            .toString() + unitName
    }
}