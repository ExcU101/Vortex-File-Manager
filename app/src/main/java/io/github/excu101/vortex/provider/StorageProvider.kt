package io.github.excu101.vortex.provider

import android.content.Context
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.FileUnit
import io.github.excu101.filesystem.fs.path.Path
import javax.inject.Inject

class StorageProvider @Inject constructor(private val context: Context) {

    fun provideList(path: Path): List<FileUnit> = FileProvider.newDirStream(path)
        .use { it.toList() }
        .map { FileUnit(it) }

}