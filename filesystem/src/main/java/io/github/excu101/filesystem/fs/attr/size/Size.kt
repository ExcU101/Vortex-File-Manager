package io.github.excu101.filesystem.fs.attr.size

import java.math.BigInteger
import java.text.DecimalFormat

class Size(val memory: BigInteger) {

    constructor(bytes: Long) : this(bytes.toBigInteger())

    companion object {
        const val simplePattern = "#"

        private infix fun Int.pow(n: Int): BigInteger {
            if (n <= 0) {
                return BigInteger.ONE
            }

            return this.toBigInteger() * pow(n - 1)
        }
    }

    enum class BinaryPrefixes(val value: BigInteger) {
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

    enum class SiPrefixes(val value: BigInteger) {
        B(1L.toBigInteger()),
        KB(B.value.multiply(1000.toBigInteger())),
        MB(KB.value.multiply(1000.toBigInteger())),
        GB(MB.value.multiply(1000.toBigInteger())),
        TB(GB.value.multiply(1000.toBigInteger())),
        PB(TB.value.multiply(1000.toBigInteger())),
        EB(PB.value.multiply(1000.toBigInteger())),
        ZB(EB.value.multiply(1000.toBigInteger())),
        YB(ZB.value.multiply(1000.toBigInteger())),
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

    fun isEmpty(): Boolean = memory > BigInteger.ZERO

    fun toBinaryType(): String {
        if (memory < 0.toBigInteger()) throw IllegalArgumentException("Invalid file size: $memory")

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
        if (memory < 0.toBigInteger()) throw IllegalArgumentException("Invalid file size: $memory")

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

    private fun formatSize(divider: BigInteger, unitName: String): String {
        return DecimalFormat(pattern)
            .format(memory.div(divider))
            .toString() + unitName
    }
}