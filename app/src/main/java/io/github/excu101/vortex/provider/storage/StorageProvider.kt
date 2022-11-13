package io.github.excu101.vortex.provider.storage

import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.data.PathItem
import kotlinx.coroutines.flow.Flow

interface StorageProvider {

    suspend fun getItems(item: PathItem): Flow<PathItem>

    fun copyPath(path: Path)

    fun requiresNotificationsAccess(): Boolean

    fun requiresFullStorageAccess(): Boolean

    fun requiresPermissions(): Boolean

}