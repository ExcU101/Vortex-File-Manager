package io.github.excu101.vortex.provider.storage.task

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.getSystemService
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.provider.TaskManager

class CopyPathTask(
    private val context: Context,
    private val path: Path,
) : TaskManager.AsyncTask() {

    override fun perform() {
        val service = context.getSystemService<ClipboardManager>()!!

        service.setPrimaryClip(ClipData.newPlainText("Path", path.toString()))
    }

}