package io.github.excu101.vortex

import android.content.res.Configuration.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import io.github.excu101.pluginsystem.common.DefaultOperationsPlugin
import io.github.excu101.pluginsystem.provider.PluginManager
import io.github.excu101.vortex.ui.component.theme.value.initVortexDarkColorValues
import io.github.excu101.vortex.ui.component.theme.value.initVortexDimenValues
import io.github.excu101.vortex.ui.component.theme.value.initVortexLightColorValues
import io.github.excu101.vortex.ui.component.theme.value.initVortexTextValues

@HiltAndroidApp
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        when (val mode = resources.configuration.uiMode and UI_MODE_NIGHT_MASK) {
            UI_MODE_NIGHT_YES -> {
                initVortexDarkColorValues()
            }
            UI_MODE_NIGHT_NO -> {
                initVortexLightColorValues()
            }

            else -> initVortexDarkColorValues()
        }

        initVortexDimenValues()
        initVortexTextValues()
        PluginManager.activate(DefaultOperationsPlugin())
    }

}