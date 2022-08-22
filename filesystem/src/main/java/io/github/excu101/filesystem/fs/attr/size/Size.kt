package io.github.excu101.filesystem.fs.attr.size

import java.text.DecimalFormat


class Size(val inputMemory: Long) {

    enum class BinaryPrefixes(val value: Long) {
        B(1L),
        KiB(B.value shl 10),
        MiB(KiB.value shl 10),
        GiB(MiB.value shl 10),
        TiB(GiB.value shl 10),
        PiB(TiB.value shl 10),
        EiB(PiB.value shl 10),
        ZiB(EiB.value shl 10),
        YiB(ZiB.value shl 10)
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

    fun isEmpty(): Boolean = inputMemory == 0L

    fun toBinaryType(): String {
        if (inputMemory < 0) throw IllegalArgumentException("Invalid file size: $inputMemory")

        val type = BinaryPrefixes.values().fold(BinaryPrefixes.B) { prev, current ->
            if (inputMemory >= current.value) {
                current
            } else {
                prev
            }
        }

        return formatSize(type.value, type.name)
    }

    fun toSiType(): String {
        if (inputMemory < 0) throw IllegalArgumentException("Invalid file size: $inputMemory")

        val type = SiPrefixes.values().fold(SiPrefixes.B) { prev, current ->
            if (inputMemory >= current.value) {
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
        return DecimalFormat("#")
            .format(inputMemory.toDouble() / divider)
            .toString() + unitName
    }
}