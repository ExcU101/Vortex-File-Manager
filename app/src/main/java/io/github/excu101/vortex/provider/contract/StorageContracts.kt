package io.github.excu101.vortex.provider.contract

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Environment
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi

object Contracts {
    @RequiresApi(Build.VERSION_CODES.R)
    val FullStorageAccess = FullStorageAccessContract()

    val Permission = PermissionContract()
}

@RequiresApi(30)
class FullStorageAccessContract : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(context: Context, input: Unit): Intent =
        Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return Environment.isExternalStorageManager()
    }
}

@RequiresApi(33)
fun PermissionContract(): ActivityResultContract<String, Boolean> {
    return ActivityResultContracts.RequestPermission()
}