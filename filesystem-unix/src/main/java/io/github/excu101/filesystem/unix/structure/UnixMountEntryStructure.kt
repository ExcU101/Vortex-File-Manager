package io.github.excu101.filesystem.unix.structure

internal data class UnixMountEntryStructure(
    val name: ByteArray,
    val dir: ByteArray,
    val type: ByteArray,
    val options: ByteArray,
    val dumpFrequency: Int, // in days
    val passNumber: Int,
) {
    @Volatile
    private var cachedName: String? = null

    @Volatile
    private var cachedDir: String? = null

    @Volatile
    private var cachedType: String? = null

    fun getCachedName(): String {
        if (cachedName == null) {
            cachedName = name.decodeToString()
        }

        return cachedName!!
    }

    fun getCachedDir(): String {
        if (cachedDir == null) {
            cachedDir = dir.decodeToString()
        }

        return cachedDir!!
    }

    fun getCachedType(): String {
        if (cachedType == null) {
            cachedType = type.decodeToString()
        }

        return cachedType!!
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UnixMountEntryStructure

        if (!name.contentEquals(other.name)) return false
        if (!dir.contentEquals(other.dir)) return false
        if (!type.contentEquals(other.type)) return false
        if (!options.contentEquals(other.options)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.contentHashCode()
        result = 31 * result + dir.contentHashCode()
        result = 31 * result + type.contentHashCode()
        result = 31 * result + options.contentHashCode()
        return result
    }
}