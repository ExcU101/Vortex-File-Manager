package io.github.excu101.vortex.data.storage

import io.github.excu101.vortex.data.storage.impl.ItemCountPartInfoParsers
import io.github.excu101.vortex.data.storage.impl.LastModifiedTimePartInfoParser
import io.github.excu101.vortex.data.storage.impl.MimeTypeInfoParser
import io.github.excu101.vortex.data.storage.impl.SizePartInfoParser

object PathItemContentParsers : Iterable<PathItemPartInfoParser> {

    override fun iterator() = parsers.iterator()

    val ItemCount = ItemCountPartInfoParsers()
    val LastModifiedTime = LastModifiedTimePartInfoParser()
    val Size = SizePartInfoParser()
    val MimeType = MimeTypeInfoParser()

    fun register(parser: PathItemPartInfoParser) {
        parsers.add(parser)
    }

    fun register(at: Int, parser: PathItemPartInfoParser) {
        if (at <= -1) return
        parsers.add(at, parser)
    }

    private val parsers = mutableListOf(
        ItemCount,
        LastModifiedTime,
        Size,
        MimeType
    )
}

infix operator fun PathItemContentParsers.plus(parser: PathItemPartInfoParser) = register(parser)
