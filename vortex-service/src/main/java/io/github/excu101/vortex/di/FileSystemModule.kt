package io.github.excu101.vortex.di

import dagger.Module
import dagger.Provides
import io.github.excu101.filesystem.fs.FileSystem
import io.github.excu101.filesystem.unix.UnixFileSystem
import javax.inject.Singleton

@Module
object FileSystemModule {

    @Provides
    @Singleton
    fun createLinuxFileSystem(): FileSystem = UnixFileSystem()

}