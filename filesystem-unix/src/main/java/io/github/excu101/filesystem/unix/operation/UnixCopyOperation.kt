package io.github.excu101.filesystem.unix.operation

import android.system.OsConstants.O_RDONLY
import android.system.OsConstants.O_WRONLY
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.CopyAction
import io.github.excu101.filesystem.unix.UnixCalls
import io.github.excu101.filesystem.unix.UnixCalls.fstat

internal class UnixCopyOperation(
    private val sources: Set<Path>,
    private val dest: Path,
) : FileOperation() {

    override suspend fun perform() {

        sources.forEach { source ->
            action(CopyAction(source, dest))
            val sourceFileDescriptor = UnixCalls.open(
                path = source.bytes,
                flags = O_RDONLY,
                mode = 0
            )

            if (sourceFileDescriptor == null) {
                error(Throwable("Couldn't open source descriptor"))
                return
            }

            val destFileDescriptor = UnixCalls.open(
                path = dest.bytes,
                flags = O_WRONLY,
                mode = fstat(sourceFileDescriptor).mode
            )

            if (destFileDescriptor == null) {
                error(Throwable("Couldn't open dest descriptor"))
                return
            }

            while (true) {
                val wroteBytes = UnixCalls.moveBytes(
                    fromDescriptor = sourceFileDescriptor,
                    toDescriptor = destFileDescriptor,
                    null,
                    8 * 1024
                )

                if (wroteBytes == 0L) break
            }
        }
        completion()
    }
}