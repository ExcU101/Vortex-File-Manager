package io.github.excu101.filesystem.fs.attr.size


interface Size {

    companion object {
        const val NoType = -1
        const val Bit = 0
        const val KiB = 1
        const val MiB = 2
        const val GiB = 3
        const val TiB = 4
        const val PiB = 5
        const val EiB = 6
        const val ZiB = 7
        const val YiB = 8
    }

    val original: Long

    val type: Int

    val name: String

    val delimiter: Int

    operator fun plus(other: Size): Size

    operator fun minus(other: Size): Size

    override fun equals(other: Any?): Boolean

    override fun toString(): String
}