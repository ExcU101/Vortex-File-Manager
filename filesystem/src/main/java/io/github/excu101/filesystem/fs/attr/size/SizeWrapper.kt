package io.github.excu101.filesystem.fs.attr.size

import io.github.excu101.filesystem.fs.attr.size.Size.Companion.Bit
import io.github.excu101.filesystem.fs.attr.size.Size.Companion.KiB
import io.github.excu101.filesystem.fs.attr.size.Size.Companion.NoType
import java.text.DecimalFormat
import kotlin.math.pow

abstract class SizeWrapper(
    override val original: Long,
) : Size {

    final override var type: Int = NoType
        protected set

    override val name: String
        get() = onCreateTypeTitle(type)

    override fun plus(other: Size): Size = onCreateSize(original + other.original)

    override fun minus(other: Size): Size = onCreateSize(
        if (other.original > original) other.original - original else original - other.original
    )

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Size) return false

        return original == other.original
    }

    override fun toString(): String {
        if (type != NoType) return createStringType()
        return toStringOriginal()
    }

    private fun toStringOriginal(): String {
        if (original < delimiter) {
            type = Bit
            return "$original${onCreateTypeTitle(Bit)}"
        }

        var original = this.original / delimiter
        type = KiB

        // TODO: Check if cycle unwinding makes badass things
        while (original >= delimiter) {
            if (type > 8) break
            type += 1
            original /= delimiter
        }

        return createStringType()
    }

    private fun createStringType(): String {
        return DecimalFormat("#.##").format(
            original / delimiter.toFloat().pow(type)
        ).toString() + onCreateTypeTitle(type)
    }

    abstract fun onCreateTypeTitle(type: Int): String
    abstract fun onCreateSize(original: Long): Size
}