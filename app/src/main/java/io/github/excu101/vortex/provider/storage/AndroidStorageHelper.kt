package io.github.excu101.vortex.provider.storage

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.documentfile.provider.DocumentFile
import io.github.excu101.vortex.data.PathItem
import javax.inject.Inject

private const val Scheme = "content://"
private const val Authority = "com.android.externalstorage.documents"
private const val TypeTree = "/tree"
private const val TypeDocument = "/document"
private const val Storage = "/primary%3A"
private const val Full = Scheme + Authority + TypeDocument + Storage

class AndroidStorageHelper @Inject constructor(
    private val context: Context,
) {

    @RequiresApi(Build.VERSION_CODES.R)
    fun checkRestrictedPath(item: PathItem): Boolean {
        if (!item.isDirectory) return false
        return when (item.name) {
            "obb" -> true
            "data" -> true
            else -> false
        }
    }

    fun takePersistableUriPermission(uri: Uri) {
        context.contentResolver.takePersistableUriPermission(
            uri, FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION
        )
    }

    fun readDocumentTree(uri: Uri): Array<DocumentFile> {
        val directory = DocumentFile.fromTreeUri(context, uri) ?: return emptyArray()

        return directory.listFiles()
    }

    fun buildDirectoryUri(name: String): Uri {
        val targetDirectory = Full + "Android%2F$name"

        return Uri.parse(targetDirectory)
    }

    fun containsPersist(name: String) = containsPersist(buildDirectoryUri(name))

    fun containsPersist(tree: Uri): Boolean {
        for (persist in context.contentResolver.persistedUriPermissions) {
            if (persist.uri == tree) return true
        }
        return false
    }

    @RequiresApi(30)
    fun requestRestrictedPermission(name: String?): Intent? {
        if (name == null) return null
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        val uri = buildDirectoryUri(name)
        intent.putExtra("android.provider.extra.INITIAL_URI", uri)
        intent.addFlags(
            FLAG_GRANT_READ_URI_PERMISSION
                    or FLAG_GRANT_WRITE_URI_PERMISSION
                    or FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        )

        return intent
    }

    fun requiresPermissions(): Boolean {
        return checkSelfPermission(
            context,
            READ_EXTERNAL_STORAGE
        ) != PERMISSION_GRANTED || checkSelfPermission(
            context,
            WRITE_EXTERNAL_STORAGE
        ) != PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requiresNotificationsAccess(): Boolean {
        return checkSelfPermission(
            context,
            POST_NOTIFICATIONS
        ) != PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun requiresFullStorageAccess(): Boolean = !Environment.isExternalStorageManager()


}