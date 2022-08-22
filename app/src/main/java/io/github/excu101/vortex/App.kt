package io.github.excu101.vortex

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import io.github.excu101.pluginsystem.common.DefaultOperationsPlugin
import io.github.excu101.pluginsystem.provider.PluginManager
import io.github.excu101.vortex.provider.ResourceProvider

@HiltAndroidApp
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        PluginManager.activate(DefaultOperationsPlugin())
    }

}