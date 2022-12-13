package io.github.excu101.vortex.provider.storage

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.getSystemService
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path

class CopyPathOperation(
    private val context: Context,
    private val source: Path
) : FileOperation() {

    override suspend fun perform() {
        action(CopyPathAction(source = source))

        val clipboard = context.getSystemService<ClipboardManager>()
        val label = source.getName().toString()
        val text = source.toString()

        clipboard?.setPrimaryClip(ClipData.newPlainText(label, text))

        completion()
    }

}

interface CopyPathAction : FileOperation.Action {
    val source: Path
}

@Suppress("FunctionName")
fun CopyPathAction(source: Path): FileOperation.Action {
    return object : CopyPathAction {
        override val source: Path = source
    }
}