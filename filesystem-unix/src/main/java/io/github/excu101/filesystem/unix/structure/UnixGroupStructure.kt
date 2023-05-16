package io.github.excu101.filesystem.unix.structure

data class UnixGroupStructure(
    val name: ByteArray,
    val password: ByteArray,
    val id: Int,
    val members: Array<ByteArray>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UnixGroupStructure

        if (!name.contentEquals(other.name)) return false
        if (!password.contentEquals(other.password)) return false
        if (id != other.id) return false
        if (!members.contentDeepEquals(other.members)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.contentHashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + id
        result = 31 * result + members.contentDeepHashCode()
        return result
    }
}