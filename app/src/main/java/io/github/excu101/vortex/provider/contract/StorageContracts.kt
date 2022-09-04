package io.github.excu101.vortex.provider.contract

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import io.github.excu101.vortex.BuildConfig

@RequiresApi(30)
class FullStorageAccessContract : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
        return Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION, uri)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return Environment.isExternalStorageManager()
    }
}

@RequiresApi(21)
class StorageAccessContract : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return false
    }
}