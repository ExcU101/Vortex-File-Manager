package io.github.excu101.vortex.provider

import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import java.io.File

@RequiresApi(Build.VERSION_CODES.Q)
object SafResolver {

    val restrictedDirs = arrayOf(
        File(Environment.getExternalStorageDirectory(), "Android/data").absolutePath,
        File(Environment.getExternalStorageDirectory(), "Android/obb").absolutePath
    )

    private const val storageType = "primary"
    private const val androidDocumentsAuthority =
        "com.android.externalstorage.documents"

    fun resolveUri(
        path: String,
    ): String? {
        return if (restrictedDirs.contains(path)) {
            val suffix = path.substringAfter(Environment.getExternalStorageDirectory().absolutePath)
            val documentId = "$storageType:${suffix.substring(1)}"

            DocumentsContract.buildDocumentUri(
                androidDocumentsAuthority,
                documentId
            ).toString()
        } else {
            null
        }
    }


}