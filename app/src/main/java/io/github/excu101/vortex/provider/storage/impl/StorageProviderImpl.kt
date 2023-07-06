package io.github.excu101.vortex.provider.storage.impl

import android.content.ClipData.newPlainText
import android.content.ClipboardManager
import android.content.Context
import android.os.Environment.getExternalStorageDirectory
import androidx.core.content.getSystemService
import io.github.excu101.filesystem.fs.PathObserver
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.fs.utils.flow
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.storage.StorageFileObserver
import io.github.excu101.vortex.provider.storage.StorageProvider
import io.github.excu101.vortex.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StorageProviderImpl @Inject constructor(
    private val context: Context,
    private val dispatchers: DispatcherProvider,
) : StorageProvider {

    companion object {
        val EXTERNAL_STORAGE = getExternalStorageDirectory().asPath()
    }

    private val observers = mutableMapOf<Path, StorageFileObserver>()

    override fun registerObserver(
        path: Path,
        types: Int,
    ): PathObserver {
        val unit = StorageFileObserver(path = path, types = types)
        observers[path] = unit
        return unit
    }

    override fun hasObserver(path: Path): Boolean = observers[path] != null


    override fun closeObserver(
        path: Path,
    ) {
        observers.remove(path)?.cancel()
    }

    override fun closeObservers() {
        observers.forEach { (path, observer) ->
            observer.cancel()
            observers.remove(path)
        }
    }

    override fun copyPath(path: Path) {
        context.getSystemService<ClipboardManager>()?.setPrimaryClip(
            newPlainText("Path", path.toString())
        )
    }

    override suspend fun getItems(item: PathItem): Flow<PathItem> {
        return item.value.flow.map(::PathItem).flowOn(dispatchers.io)
    }

}