package io.github.excu101.vortex.provider

import android.Manifest.permission.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.getSystemService
import io.github.excu101.filesystem.FileProvider.newDirStream
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.fs.utils.resolve
import io.github.excu101.vortex.data.PathItem
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class StorageProvider @Inject constructor(
    private val context: Context,
    private val workContext: CoroutineContext = IO,
) {

    companion object {
        val EXTERNAL_STORAGE = getExternalStorageDirectory().asPath()
        val ANDROID_OBB = EXTERNAL_STORAGE resolve  "Android/obb"
        val ANDROID_DATA = EXTERNAL_STORAGE resolve "Android/data"
    }

    fun requiresPermissions(): Boolean {
        return !(checkSelfPermission(
            context,
            READ_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED && checkSelfPermission(
            context,
            WRITE_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requiresNotificationsAccess(): Boolean {
        return checkSelfPermission(
            context,
            POST_NOTIFICATIONS
        ) != PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun requiresFullStorageAccess(): Boolean {
        return !Environment.isExternalStorageManager()
    }

    fun copyPath(item: PathItem) {
        val service = context.getSystemService<ClipboardManager>()

        service?.setPrimaryClip(ClipData.newPlainText("Path", item.path))
    }

    suspend fun provideList(item: PathItem): List<PathItem> = withContext(workContext) {
        try {
            newDirStream(item.value).use { stream ->
                val items = mutableListOf<PathItem>()
                for (it in stream) {
                    items.add(PathItem(it))
                }
                items
            }
        } catch (error: Throwable) {
            throw error
        }
    }
}