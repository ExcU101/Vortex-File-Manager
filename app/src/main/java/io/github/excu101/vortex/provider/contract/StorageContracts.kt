package io.github.excu101.vortex.provider.contract

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi

object Contracts {

    @RequiresApi(Build.VERSION_CODES.Q)
    val RestrictedDirectoriesAccess = RestrictedDirectoriesAccess()

    @RequiresApi(Build.VERSION_CODES.R)
    val FullStorageAccess = FullStorageAccessContract()

    val Permission = PermissionContract()
}

@RequiresApi(Build.VERSION_CODES.Q)
class RestrictedDirectoriesAccess : ActivityResultContract<Uri, Uri?>() {
    override fun createIntent(context: Context, input: Uri): Intent {
        ActivityResultContracts.StartActivityForResult()
        return Intent(
            Intent.ACTION_OPEN_DOCUMENT_TREE
        ).putExtra(DocumentsContract.EXTRA_INITIAL_URI, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (intent == null) return null
        if (resultCode != RESULT_OK) return null
        intent.dataString

        return intent.data
    }

}

@RequiresApi(Build.VERSION_CODES.R)
class FullStorageAccessContract : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(context: Context, input: Unit): Intent =
        Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {

            data = Uri.fromParts("package", context.packageName, null)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return Environment.isExternalStorageManager()
    }
}

fun PermissionContract(): ActivityResultContract<String, Boolean> {
    return ActivityResultContracts.RequestPermission()
}