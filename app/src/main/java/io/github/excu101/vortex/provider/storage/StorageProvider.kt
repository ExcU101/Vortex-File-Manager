package io.github.excu101.vortex.provider.storage

import android.Manifest.permission.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.getSystemService
import io.github.excu101.filesystem.FileProvider.newDirStream
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.fs.utils.resolve
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.TaskManager
import io.github.excu101.vortex.provider.TaskManager.AsyncTask
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class StorageProvider @Inject constructor(
    val context: Context,
    private val workContext: CoroutineContext = IO,
) : TaskManager<AsyncTask> {

    companion object {
        val EXTERNAL_STORAGE = getExternalStorageDirectory().asPath()
        val ANDROID_OBB = EXTERNAL_STORAGE resolve "Android/obb"
        val ANDROID_DATA = EXTERNAL_STORAGE resolve "Android/data"

        val restrictedDirs = arrayOf(
            ANDROID_OBB,
            ANDROID_DATA
        )

        private const val storageType = "primary"
        private const val androidDocumentsAuthority =
            "com.android.externalstorage.documents"
    }

    private val tasks = mutableMapOf<KClass<out AsyncTask>, AsyncTask>()

    fun resolveSafUri(
        path: Path,
    ): String? {
        val cPath = path.toString()
        return if (restrictedDirs.contains(path)) {
            val suffix =
                cPath.substringAfter(getExternalStorageDirectory().absolutePath)
            val documentId = "$storageType:${suffix.substring(1)}"

            DocumentsContract.buildDocumentUri(
                androidDocumentsAuthority,
                documentId
            ).toString()
        } else {
            null
        }
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

    fun createContextTask() {

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

    override fun registerTask(task: AsyncTask): StorageProvider {
        tasks[task::class] = task
        return this
    }

    override fun performTask(clazz: KClass<AsyncTask>): StorageProvider {
        tasks[clazz]?.perform()
        return this
    }
}