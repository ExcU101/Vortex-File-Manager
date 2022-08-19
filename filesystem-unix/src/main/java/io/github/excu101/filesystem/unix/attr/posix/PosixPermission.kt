package io.github.excu101.filesystem.unix.attr.posix

interface PosixPermission {

    companion object {
        val readPerms = listOf(Owner.READ_OWNER, Group.READ_GROUP, Other.READ_OTHER)
        val writePerms = listOf(Owner.WRITE_OWNER, Group.WRITE_GROUP, Other.WRITE_OTHER)
        val executePerms = listOf(Owner.EXECUTE_OWNER, Owner.EXECUTE_OWNER, Other.EXECUTE_OTHER)
    }

    enum class Owner : PosixPermission {
        READ_OWNER,
        WRITE_OWNER,
        EXECUTE_OWNER,
    }

    enum class Group : PosixPermission {
        READ_GROUP,
        WRITE_GROUP,
        EXECUTE_GROUP,
    }

    enum class Other : PosixPermission {
        READ_OTHER,
        WRITE_OTHER,
        EXECUTE_OTHER,
    }

}