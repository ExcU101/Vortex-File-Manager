package io.github.excu101.filesystem.unix.error

class UnixException(
    private val errno: Int,
    val name: String,
) : Throwable("$errno: $name") {

    val error: Error?
        get() = Error.values().find { it.value == errno }

    enum class Error(val value: Int) {
        ENOENT(value = 2),
        EINTR(value = 4),
        EIO(value = 5),
        EBADF(value = 9),
        ENOMEM(value = 12),
        EACCES(value = 13),
        EEXIST(value = 17),
        ENOTDIR(value = 20),
        ENOSPC(value = 28),
        EROFS(value = 30),
        EMLINK(value = 31),
        ENAMETOOLONG(value = 36),
        ENOSYS(value = 38),
        ELOOP(value = 40),
        EOVERFLOW(value = 75)
    }

}