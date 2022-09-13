package io.github.excu101.filesystem.fs.attr.size

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
        B(2 pow 3),
        KiB(2 pow 10),
        MiB(2 pow 20),
        GiB(2 pow 30),
        TiB(2 pow 40),
        PiB(2 pow 50),
        EiB(2 pow 60),
        ZiB(2 pow 70),
        YiB(2 pow 80)
    }

    enum class SiPrefixes(val value: Long) {
        B(1L),
        KB(B.value * 1000),
        MB(KB.value * 1000),
        GB(MB.value * 1000),
        TB(GB.value * 1000),
        PB(TB.value * 1000),
        EB(PB.value * 1000),
        ZB(EB.value * 1000),
        YB(ZB.value * 1000),
    }

    var pattern = simplePattern

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
        return DecimalFormat(pattern)
            .format(memory / divider)
            .toString() + unitName
    }
}