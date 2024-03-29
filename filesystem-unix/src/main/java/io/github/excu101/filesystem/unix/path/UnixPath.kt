package io.github.excu101.filesystem.unix.path

import io.github.excu101.filesystem.fs.FileSystem
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.UnixFileSystem
import kotlin.math.min


class UnixPath internal constructor(
    private val path: ByteArray,
    private val _system: UnixFileSystem,
) : Path {

    private val points: List<Int>
    private var cachedHash: Int? = null
    private var cachedPath: String? = null

    init {
        val list = mutableListOf<Int>()

        if (!isEmpty) {
            if (path.size > 1) {
                path.forEachIndexed { index, byte ->
                    if (byte == system.separator) {
                        list.add(index)
                    }
                }
            }
        }

        points = list
    }

    override val root: Path?
        get() = if (isAbsolute) _system.rootDirectory else null


    override val parent: Path?
        get() = when (nameCount) {
            0 -> null
            1 -> root
            else -> root!!.resolve(sub(0, points[nameCount - 1]))
        }

    override fun getParentAt(index: Int): Path? {
        if (nameCount == 0) return null
        if (nameCount == 1) return root

        if (points.isEmpty()) return null
        if (index < 0) return null
        if (index >= points.size) return null

        return root!!.resolve(sub(0, points[index]))
    }

    override val length: Int
        get() = path.size

    override val isEmpty: Boolean
        get() = path.isEmpty()

    override val isHidden: Boolean
        get() = _system.provider.isHidden(source = this)

    override val nameCount: Int
        get() = points.size

    override val isAbsolute: Boolean
        get() = !isEmpty && path[0] == system.separator

    override val bytes: ByteArray
        get() = path

    override val system: FileSystem
        get() = _system

    override val scheme: String
        get() = system.scheme

    override fun startsWith(other: Path): Boolean {
        if (other.isEmpty) return false
        if (isEmpty) return false

        for (i in 0..other.length) {
            val segment1 = bytes[i]
            val segment2 = other.bytes[i]

            if (segment1 != segment2) return false
        }

        return true
    }

    override fun endsWith(other: Path): Boolean {
        if (other.isEmpty) return false
        if (isEmpty) return false

        return true
    }

    private fun resolve(base: ByteArray, child: ByteArray): ByteArray {
        val baseLength = base.size
        val childLength = child.size
        if (childLength == 0) return base
        val result: ByteArray
        if (baseLength == 1 && base[0] == system.separator) {
            result = ByteArray(childLength + 1)
            result[0] = system.separator
            System.arraycopy(child, 0, result, 1, childLength)
        } else {
            result = ByteArray(baseLength + 1 + childLength)
            System.arraycopy(base, 0, result, 0, baseLength)
            result[base.size] = system.separator
            System.arraycopy(child, 0, result, baseLength + 1, childLength)
        }
        return result
    }


    override fun resolve(other: Path): Path {
        if (isEmpty) return other

        if (other.isAbsolute) return other

        if (other.isEmpty) return this

        return UnixPath(
            path = resolve(bytes, other.bytes),
            _system = _system
        )
    }

    override fun normalize(): Path {
        val normalizedSegments = mutableListOf<Byte>()
        for (byte in bytes) {
            if (byte == '.'.code.toByte()) {
//
            } else if (byte == "..".toByte()) {
                if (normalizedSegments.isEmpty()) {
                    if (!isAbsolute) {
                        normalizedSegments += byte
                    }
                } else {
                    if (normalizedSegments.last() == "..".toByte()) {
                        normalizedSegments += byte
                    } else {
                        normalizedSegments.removeLast()
                    }
                }
            } else {
                normalizedSegments += byte
            }
        }
        if (!isAbsolute && normalizedSegments.isEmpty()) {
            return UnixPath(_system = _system, path = byteArrayOf(0))
        }

        return UnixPath(_system = _system, path = normalizedSegments.toByteArray())
    }

    override fun relativize(other: Path): Path {
        require(isAbsolute == other.isAbsolute) {
            "The other path must be as absolute as this path"
        }
        if (other == this) return UnixPath(byteArrayOf(0), _system)
        if (isEmpty) return other
//        if (other.isEmpty) return this
        val size = length
        val otherSize = other.length
        val minimal = min(size, otherSize)
        var common = 0
        while (common < minimal && bytes[common] == other.bytes[common]) {
            ++common
        }
        val relatives = mutableListOf<Byte>()
        val dotCount = size - common

        if (dotCount > 0) {
            repeat(dotCount) { relatives += "..".toByte() }
        }
        if (common < otherSize) {
            relatives.addAll(other.bytes.copyOfRange(common, otherSize).toTypedArray())
        }

        return UnixPath(relatives.toByteArray(), _system)
    }

    override fun sub(from: Int, to: Int): Path {
        if (from == 0 && to == length) {
            return this
        }
        return UnixPath(path = path.copyOfRange(from, to), _system = _system)
    }

    override fun getName(index: Int): Path {
        if (nameCount == 0) return UnixPath(
            path = byteArrayOf(system.separator),
            _system = _system
        )

        val begin = points[index] + 1
        val end = points.getOrElse(index = index + 1, defaultValue = { length })

        return sub(from = begin, to = end)
    }

    override fun toString(): String {
        if (cachedPath == null) {
            cachedPath = path.decodeToString()
        }
        return cachedPath!!
    }


    override fun compareTo(other: Path): Int {

        return other.length - length
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UnixPath

        if (!bytes.contentEquals(other.bytes)) return false
        if (system != other.system) return false

        return true
    }

    override fun hashCode(): Int {
        if (cachedHash == null) {
            cachedHash = bytes.contentHashCode()
        }
        return cachedHash!!
    }

}