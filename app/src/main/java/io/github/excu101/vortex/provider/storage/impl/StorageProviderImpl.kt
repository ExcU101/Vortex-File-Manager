package io.github.excu101.vortex.provider.storage.impl

import android.Manifest.permission.*
import android.content.ClipData.newPlainText
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Environment.getExternalStorageDirectory
import android.os.Environment.isExternalStorageManager
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.getSystemService
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.fs.utils.flow
import io.github.excu101.filesystem.fs.utils.resolve
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.storage.StorageProvider
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class StorageProviderImpl @Inject constructor(
    val context: Context,
    private val workContext: CoroutineContext = IO,
) : StorageProvider {

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

    override fun requiresPermissions(): Boolean {
        return !(checkSelfPermission(
            context,
            READ_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED && checkSelfPermission(
            context,
            WRITE_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun requiresNotificationsAccess(): Boolean {
        return checkSelfPermission(
            context,
            POST_NOTIFICATIONS
        ) != PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun requiresFullStorageAccess(): Boolean = !isExternalStorageManager()


    override fun copyPath(path: Path) {
        context.getSystemService<ClipboardManager>()?.setPrimaryClip(
            newPlainText("Path", path.toString())
        )
    }

    override suspend fun getItems(item: PathItem): Flow<PathItem> {
        return item.value.flow.map(::PathItem).flowOn(workContext)
    }

}