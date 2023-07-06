package io.github.excu101.vortex.provider.contract

import android.Manifest
import android.content.Intent
import android.net.Uri.fromParts
import android.os.Build
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions.Companion.ACTION_REQUEST_PERMISSIONS
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions.Companion.EXTRA_PERMISSIONS
import androidx.annotation.RequiresApi
import io.github.excu101.vortex.BuildConfig.APPLICATION_ID

object Contracts {
    object Storage {
        const val RequestStorageAccessCode = 0
        const val RequestFullStorageAccessCode = 1
        const val RequestRestrictedDirectoryAccess = 3

        fun StorageAccessIntent() = Intent(ACTION_REQUEST_PERMISSIONS).putExtra(
            EXTRA_PERMISSIONS,
            Manifest.permission_group.STORAGE
        )

        @RequiresApi(Build.VERSION_CODES.R)
        fun StorageFullAccessIntent() =
            Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).setData(
                fromParts("package", APPLICATION_ID, null)
            ).addCategory("android.intent.category.DEFAULT")
    }
}