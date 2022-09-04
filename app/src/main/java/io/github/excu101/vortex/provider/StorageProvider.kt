package io.github.excu101.vortex.provider

import android.content.Context
import android.os.Environment.getExternalStorageDirectory
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.FileProvider.newDirStream
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.fs.utils.resolve
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StorageProvider @Inject constructor(private val context: Context) {

    companion object {
        val EXTERNAL_STORAGE = getExternalStorageDirectory().asPath()
        val ANDROID_OBB = EXTERNAL_STORAGE resolve "Android/obb"
        val ANDROID_DATA = EXTERNAL_STORAGE resolve "Android/data"
    }

    suspend fun provideList(path: Path): List<Path> =
        withContext(IO) {
            newDirStream(path).use {
                it.toList()
            }
        }
}