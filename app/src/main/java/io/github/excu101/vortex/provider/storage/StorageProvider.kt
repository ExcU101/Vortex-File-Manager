package io.github.excu101.vortex.provider.storage

import io.github.excu101.filesystem.fs.DirectoryObserver
import io.github.excu101.filesystem.fs.observer.PathObservableEventType
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.data.PathItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StorageProvider {

    suspend fun getItems(item: PathItem): Flow<PathItem>

    val tasks: StateFlow<List<Task>>

    suspend fun registerTask(task: Task)

    suspend fun unregisterTask(task: Task)

    fun registerObserver(
        directory: Path,
        vararg types: PathObservableEventType,
    ): DirectoryObserver

    fun closeObserver(
        directory: Path,
        onRemove: (StorageDirectoryObserver) -> Unit
    )

    fun closeObservers()

    fun copyPath(path: Path)

    fun requiresNotificationsAccess(): Boolean

    fun requiresFullStorageAccess(): Boolean

    fun requiresPermissions(): Boolean

}