package io.github.excu101.filesystem.fs.attr.size

import io.github.excu101.filesystem.fs.attr.size.Size.Companion.Bit
import io.github.excu101.filesystem.fs.attr.size.Size.Companion.EiB
import io.github.excu101.filesystem.fs.attr.size.Size.Companion.GiB
import io.github.excu101.filesystem.fs.attr.size.Size.Companion.KiB
import io.github.excu101.filesystem.fs.attr.size.Size.Companion.MiB
import io.github.excu101.filesystem.fs.attr.size.Size.Companion.PiB
import io.github.excu101.filesystem.fs.attr.size.Size.Companion.TiB
import io.github.excu101.filesystem.fs.attr.size.Size.Companion.YiB
import io.github.excu101.filesystem.fs.attr.size.Size.Companion.ZiB

@Suppress("FunctionName")
fun SiSize(
    original: Long,
): Size = object : SizeWrapper(original) {
    override fun onCreateSize(original: Long): Size = SiSize(original)
    override val delimiter: Int = 1000
    override fun onCreateTypeTitle(type: Int) = when (type) {
        Bit -> "B"
        KiB -> "KiB"
        MiB -> "MiB"
        GiB -> "GiB"
        TiB -> "TiB"
        PiB -> "PiB"
        EiB -> "EiB"
        ZiB -> "ZiB"
        YiB -> "YiB"
        else -> throw UnsupportedOperationException("Unsupported type of size")
    }
}