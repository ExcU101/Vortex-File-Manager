package io.github.excu101.filesystem.unix.operation

import android.system.OsConstants
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.option.Options
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.CopyAction
import io.github.excu101.filesystem.fs.utils.flow
import io.github.excu101.filesystem.unix.UnixCalls
import io.github.excu101.filesystem.unix.observer.UnixMasks.maskWith
import io.github.excu101.filesystem.unix.structure.UnixStatusStructure
import io.github.excu101.filesystem.unix.utils.S_IFDIR
import io.github.excu101.filesystem.unix.utils.S_IFLNK
import io.github.excu101.filesystem.unix.utils.S_IFMT
import io.github.excu101.filesystem.unix.utils.S_IFREG
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

internal class UnixCutOperation(
    private var sources: Set<Path>,
    private val dest: Path,
    options: Int,
) : FileOperation() {

    private val noFollowLinks = options maskWith Options.Copy.NoFollowLinks
    private val replaceExists = options maskWith Options.Copy.ReplaceExists

    override suspend fun perform() {
        sources.forEach { source ->
            action(CopyAction(source, dest))
            resolveOp(source)
        }
        UnixDeleteOperation(sources).perform()
        completion()
    }

    private suspend inline fun resolveOp(source: Path) = with(
        UnixCalls.stat(
            source.bytes,
            noFollowLinks
        )
    ) {
        when (mode and S_IFMT) {
            S_IFDIR -> copyDir(
                source = source,
                status = this,
            )

            S_IFREG -> copyFile(
                source = source,
                status = this,
            )

            S_IFLNK -> copyLink(
                source = source,
                status = this,
            )

            else -> {}
        }

    }

    @Suppress("NOTHING_TO_INLINE")
    private suspend inline fun copyFile(source: Path, status: UnixStatusStructure) {
        val dest = resolveSourceWithDest(source)

        val sourceFileDescriptor = UnixCalls.open(
            path = source.bytes,
            flags = OsConstants.O_RDONLY,
            mode = 0
        )

        if (sourceFileDescriptor == null) {
            error(Throwable("Couldn't open source descriptor"))
            return
        }

        var destFlags = OsConstants.O_WRONLY or OsConstants.O_CREAT or OsConstants.O_TRUNC
        if (replaceExists) {
            destFlags = destFlags or OsConstants.O_EXCL
        }
        val destFileDescriptor = UnixCalls.open(
            path = dest.bytes,
            flags = destFlags,
            mode = status.mode
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
                8192
            )

            if (wroteBytes <= 0L) break
        }

        UnixCalls.close(UnixCalls.getIndexDescriptor(destFileDescriptor))
        UnixCalls.close(UnixCalls.getIndexDescriptor(sourceFileDescriptor))
    }

    private suspend fun copyDir(source: Path, status: UnixStatusStructure) {
        val content = source.flow.map { resolveSourceWithDest(source, it) }.toList()
        val dir = dest.resolve(source.getName())

        UnixCalls.mkdir(dir.bytes, status.mode)
        content.forEach { entry -> resolveOp(entry) }
    }

    private suspend inline fun copyLink(source: Path, status: UnixStatusStructure) {

    }

    private fun resolveSourceWithDest(source: Path, entry: Path): Path {
        return dest.resolve(source.relativize(entry))
    }

    private fun resolveSourceWithDest(source: Path): Path {
        return dest.resolve(source.getName())
    }
}