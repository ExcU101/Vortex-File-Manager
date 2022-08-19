package io.github.excu101.vortex

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.unix.UnixFileSystem
import io.github.excu101.filesystem.unix.UnixFileSystemProvider
import io.github.excu101.pluginsystem.common.DefaultOperationsPlugin
import io.github.excu101.pluginsystem.provider.PluginManager

@HiltAndroidApp
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        PluginManager.activate(DefaultOperationsPlugin())
    }

}