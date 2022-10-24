package io.github.excu101.manifest.dsl.source

class PermissionScope {

    private val userPermissions = mutableListOf<String>()

    fun user(name: String) {
        userPermissions.add(name)
    }

}

inline val PermissionScope.READ_EXTERNAL_STORAGE: String
    get() = "android.permission.READ_EXTERNAL_STORAGE"

inline val PermissionScope.WRITE_EXTERNAL_STORAGE: String
    get() = "android.permission.WRITE_EXTERNAL_STORAGE"

inline val PermissionScope.MANAGE_EXTERNAL_STORAGE: String
    get() = "android.permission.MANAGER_EXTERNAL_STORAGE"

fun PermissionScope.storages() {
    user(READ_EXTERNAL_STORAGE)
    user(WRITE_EXTERNAL_STORAGE)
    user(MANAGE_EXTERNAL_STORAGE)
}